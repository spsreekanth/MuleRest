@RestController
public class ApiProxyController {

    private final OAuth2AuthorizedClientService clientService;

    public ApiProxyController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/proxy/{apiId}")
    public ResponseEntity<String> proxyApi(@PathVariable("apiId") String apiId,
                                           OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = clientService
                .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        String apiUrl = "https://" + apiId + ".com/api/endpoint";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(client.getAccessToken().getTokenValue());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        return response;
    }
}
