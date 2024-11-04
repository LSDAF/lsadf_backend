package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.bdd.CacheEntryType;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[CURRENCY WHEN STEP DEFINITIONS]")
public class BddCurrencyWhenStepDefinitions extends BddLoader {
    @When("^the cache is flushed$")
    public void when_the_cache_is_flushed() {
        log.info("Flushing cache...");
        this.cacheFlushService.flushCurrencies();
        this.cacheFlushService.flushStages();
    }

    @When("^a (.*) cache entry is expired$")
    public void when_a_cache_entry_is_expired(String cacheType) {
        CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
        int size = switch (cacheEntryType) {
            case CURRENCY, CURRENCY_HISTO -> currencyCache.getAllHisto().size();
            case STAGE, STAGE_HISTO -> stageCache.getAllHisto().size();
            case GAME_SAVE_OWNERSHIP -> gameSaveOwnershipCache.getAll().size();
        };
        log.info("Waiting for {} cache entry to expire...", cacheType);
        await().atMost(1200, TimeUnit.SECONDS).until(() -> {
            try {
                int newSize = switch (cacheEntryType) {
                    case CURRENCY, CURRENCY_HISTO -> currencyCache.getAllHisto().size();
                    case STAGE, STAGE_HISTO -> stageCache.getAllHisto().size();
                    case GAME_SAVE_OWNERSHIP -> gameSaveOwnershipCache.getAll().size();
                };
                return newSize < size;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @When("^we want to get the currencies for the game save with id (.*)$")
    public void when_we_want_to_get_the_currencies_for_the_game_save_with_id(String gameSaveId) {
        try {
            log.info("Getting currencies for game save with id: {}", gameSaveId);
            Currency currency = this.currencyService.getCurrency(gameSaveId);
            currencyStack.push(currency);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to set the following currencies for the game save with id (.*) with toCache to (.*)$")
    public void when_we_want_to_set_the_currencies_for_the_game_save_with_id_to_with_cache(String gameSaveId, boolean toCache, DataTable dataTable) {
        var data = dataTable.asMaps(String.class, String.class);
        assertThat(data).hasSize(1);

        Currency currency = BddUtils.mapToCurrency(data.get(0));

        try {
            log.info("Setting {} for game save with id: {}", currency, gameSaveId);
            this.currencyService.saveCurrency(gameSaveId, currency, toCache);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get the currencies of the game save with id (.*)$")
    public void when_the_user_requests_the_endpoint_to_get_the_currencies_of_the_game_save_with_id(String gameSaveId) {
        String fullPath = ControllerConstants.CURRENCY + ControllerConstants.Currency.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
            String token = jwtAuthentication.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Currency>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedCurrencyResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id (.*)$")
    public void when_the_user_requests_the_endpoint_to_set_the_currencies_of_the_game_save_with_id_to(String gameSaveId, DataTable dataTable) {
        var data = dataTable.asMaps(String.class, String.class);
        assertThat(data).hasSize(1);

        CurrencyRequest request = BddUtils.mapToCurrencyRequest(data.get(0));

        String fullPath = ControllerConstants.CURRENCY + ControllerConstants.Currency.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
            String token = jwtAuthentication.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);


            HttpEntity<CurrencyRequest> httpRequest = new HttpEntity<>(request, headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, httpRequest, buildParameterizedVoidResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
