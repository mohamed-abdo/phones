# phone number REST API
Mobile number / phone import, and validatoin REST API

# use case
REST API to import csv mobile numbers, format of number should be complient with south africa mobile numbers, csv sheet (id-> number (dirty data may be expected) and unique, number-> number (dirty data may be expected) ), api should able to fix invalid numbers, as well mark invalid numbers with reason of rejection, api should categorize number base on their validity and store them in repository, api should provide number validation end-point.

> desipite only south afirca mobile phone numbers formats should be considered-
  but this api designed to supported inversion of control over business requirments, so any other formats could be injects, 
  and impact area should only componet "enum" contains matching regex, or fixes.
  
# REST API functioanlity: 

1. Endpoint that receives a file (formatted like the one provided in the example) to persist phone numbers (take into account the column id to refer to the uniqueness of those numbers) 
  - Return the processed numbers
  - Valid numbers marked as “VALID”
  - Incorrectly formed numbers which were attempted to fix marked as FIXED (creativity in trying to fix the numbers is expected here)
  - Invalid and impossible to fix numbers marked as INVALID. `results are stored into a persistent storage (database, file)`
  - Valid numbers 
  - Fixed numbers + what was modified
  - Invalid numbers + why validation failed and/or could not be updated

2. Endpoint to return identifier of processed file results
  - Return statistical information about the uploaded file (how many were valid, fixed, invalid) 

3. Endpoint that returns details of the processed file Return the processed numbers (similarly to the previous endpoint)
  - Return identifier of processed file results
  - Return statistical information about the uploaded file

# design consideration
- REST API, with adequte HTTP status codes for the endpoints, as well as the appropriate URLs. 
- HATEOAS also used for linking in between the provided endpoints.
- SOLID design pricipal applied where required.
- Following microservice / domain first / Business rules IOC guidlines.
ex: `the purpose of the following code is to provide compile time business cases to be applied on data with side effect, as well singletone in its nature 'enum' ` 
```
public enum PhoneNumberFixer {

    ADD_PREFIX_ZERO(number -> {
        Objects.requireNonNull(number);
        return "0" + number;
    }),

    REMOVE_LEADING_ZEROS(number -> {
        Objects.requireNonNull(number);
        return number.replaceFirst(PhoneNumberRegex.LEADING_ZEROS.getRegex(), "");
    }),

    REMOVE_NON_DIGITS(number -> {
        Objects.requireNonNull(number);
        return number.replaceAll(PhoneNumberRegex.NON_DIGITS.getRegex(),"");
    }),

    REMOVE_INTERNATIONAL_WITH_LEADING_ZEROS(number -> {
        Objects.requireNonNull(number);
        return number.replaceFirst(PhoneNumberRegex.INTERNATIONAL_WITH_LEADING_ZEROS.getRegex(), "27");
    });

    final Function<String, String> fixer;

    PhoneNumberFixer(Function<String, String> fixer) {
        this.fixer = fixer;
    }

    String fixNumber(@NonNull String number) {
        Objects.requireNonNull(number);
        return this.fixer.apply(number);
    }
}

// to apply fixEngine in stream of input and git ny vaild way to fix number, i use the following:
var fixEngine = Arrays.stream(PhoneNumberFixer.values())
                    .filter(f -> this.phoneNumberValidator.isValidCellNumber(f.fixNumber(this.number)))
                    .findAny();
                 
```
- Generic ,friendly, debuggble exception handling.



# unit testing
> deferent levels of abstracted testing are applied based on best fit coverage and less coding efforts.

1. Unit tests, are covering core business domain with parameterize actual and expected results to let unit test concise with high coverage.
ex: -
```
 @ParameterizedTest
    @CsvSource(value = {
            "0736529279:true",
            "6478342944:false",
            "27826088289:true",
            "263716791426:false",
            "27718159078:true",
            "19855201547:false",
            "27717278645:true",
            "263774817994:false",
            "27724360860:true",
            "639565885094:false",
            "27827678672:true",
            "26771835182:false",
            "27713564440:true"}, delimiter = ':')
    void test_validateNumber(String number, boolean expected) {
        Assertions.assertEquals(expected, phoneService.isValidCellNumber(number));
    }
    
 @ParameterizedTest
    @CsvSource(value = {
            "270736529279:true",
            "27736529000_o:true",
            "_D0736529279:true",
            "736529279:true",
            "_D0436529279:false",
            "0436529279:false",
            "6478342944:false"}, delimiter = ':')
    void test_FixPhoneNumber(String number, boolean expected) {
        Assertions.assertEquals(expected, phoneService.tryToFixNumber(new PhoneSheet("0", number))
                .getPhoneNumberStatus() == PhoneNumberStatus.FIXED);
    }

    @ParameterizedTest
    @MethodSource("provide_test_FixPhoneNumber_statusCase")
    void test_FixPhoneNumber_statusCase(String number, PhoneNumberStatus phoneNumberStatus, RejectionReason rejectionReason) {
        var actual = phoneService.tryToFixNumber(new PhoneSheet("0", number));
        Assertions.assertEquals(phoneNumberStatus, actual.getPhoneNumberStatus());
        Assertions.assertEquals(rejectionReason, actual.getRejectionReason());
    }

    private static Stream<Arguments> provide_test_FixPhoneNumber_statusCase() {
        return Stream.of(
                Arguments.of("27713564440", PhoneNumberStatus.VALID, RejectionReason.NOT_APPLICABLE),
                Arguments.of("27736529000_o", PhoneNumberStatus.FIXED, RejectionReason.NOT_APPLICABLE),
                Arguments.of("270736529000", PhoneNumberStatus.FIXED, RejectionReason.NOT_APPLICABLE),
                Arguments.of("_D0436529279", PhoneNumberStatus.INVALID, RejectionReason.ILLEGAL_CHARS),
                Arguments.of("736529279", PhoneNumberStatus.FIXED, RejectionReason.NOT_APPLICABLE),
                Arguments.of("27713111564440", PhoneNumberStatus.INVALID, RejectionReason.TOO_MANY_DIGITS),
                Arguments.of("2773652", PhoneNumberStatus.INVALID, RejectionReason.MISSING_DIGITS),
                Arguments.of("639565885094", PhoneNumberStatus.INVALID, RejectionReason.MALFORMED),
                Arguments.of("27713564440_DELL", PhoneNumberStatus.FIXED, RejectionReason.NOT_APPLICABLE),
                Arguments.of("6395EOEOE94", PhoneNumberStatus.INVALID, RejectionReason.ILLEGAL_CHARS),
                Arguments.of("27913564440_DELL", PhoneNumberStatus.INVALID, RejectionReason.ILLEGAL_CHARS)
        );
    }

```
2. Integration tests to touch integerate different application layers.

ex:
```
 @Test
    void test_importFile() throws URISyntaxException, IOException {
        final var baseUrl = String.format("https://localhost:%d/phones/import", randomPortNumber);
        final URI uri = new URI(baseUrl);
        final var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.add("content-type", CONTENT_TYPE);
        var body = new LinkedMultiValueMap<String, Resource>();
        body.add("file", resource);
        var httpBody = new HttpEntity<>(body, headers);
        var response = restTemplate.postForEntity(uri, httpBody, UploadStats.class);
        var expectedFunc = Stream.of(HttpStatus.CREATED, HttpStatus.OK);
        Assertions.assertTrue(expectedFunc.anyMatch(s -> s.equals(response.getStatusCode())));
    }
```

3. Funcational testing to cover API end-to-end domain scenario (import, retreive, and validate) implemented by postman.

# run
`mvn spring-boot:run`
- {{srv-url}} -> https://localhost:5050/  `you can change from application.properties`
- default / ping url: {{srv-url}}/phones/ping -> ok, 200

# request - response

1- {{srv-url}}/phones/import
  - `REQUEST` body accept mulitpart/form-data "file"
  - `RESPONSE` 
    ```
    {
    "fileRef": "a5da7990-cce7-49aa-9873-f0fcdb3c9b59",
    "validNumbers": 457,
    "fixedNumbers": 68,
    "invalidNumbers": 476,
    "createdOn": "2019-07-07T23:31:21.188331",
    "_links": {
        "self": {
            "href": "https://localhost:5050/phones/file/a5da7990-cce7-49aa-9873-f0fcdb3c9b59"
        }
    }
    }
    ```
    
2- {{srv-url}}/phones/file/{{fileRef}}
  - `REQUEST` path varaible accept UUID input
  - `RESPONSE` 
    ```
    {
    "fileRef": "a5da7990-cce7-49aa-9873-f0fcdb3c9b59",
    "validNumbers": 457,
    "fixedNumbers": 68,
    "invalidNumbers": 476,
    "createdOn": "2019-07-07T23:34:01.275234",
    "_links": {
        "self": {
            "href": "https://localhost:5050/phones/file/a5da7990-cce7-49aa-9873-f0fcdb3c9b59"
        }
    }
    }
    ```
    
3- {{srv-url}}/phones/validate/750023242
  - `REQUEST` path varaible accept string input as a number to be validated
  - `RESPONSE` 
    ```
    {
    "number": "750023242",
    "fixedNumber": "0750023242",
    "phoneNumberStatus": "FIXED",
    "rejectionReason": "NOT_APPLICABLE",
    "fixer": "ADD_PREFIX_ZERO",
    "_links": {
        "self": {
            "href": "https://localhost:5050/phones/validate/750023242"
        }
    }
    }
    ```
    
    
  
