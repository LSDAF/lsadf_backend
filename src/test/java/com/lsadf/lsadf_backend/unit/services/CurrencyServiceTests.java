package com.lsadf.lsadf_backend.unit.services;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CurrencyServiceTests {
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private Cache<Currency> currencyCache;

    private final Mapper mapper = new MapperImpl();

    @BeforeEach
    public void init() {
        // Create all mocks and inject them into the service
        MockitoAnnotations.openMocks(this);

        currencyService = new CurrencyServiceImpl(currencyRepository, currencyCache, mapper);
    }

    @Test
    void get_currency_on_non_existing_gamesave_id() {
        // Arrange
        when(currencyRepository.findById(anyString())).thenReturn(Optional.empty());
        when(currencyCache.isEnabled()).thenReturn(true);

        // Assert
        assertThrows(NotFoundException.class, () -> currencyService.getCurrency("1"));
    }

    @Test
    void get_currency_on_existing_gamesave_id_when_cached() {
        // Arrange
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail("test@test.com")
                .goldAmount(1L)
                .diamondAmount(2L)
                .emeraldAmount(3L)
                .amethystAmount(4L)
                .build();

        Currency currency = Currency.builder()
                .gold(1L)
                .diamond(2L)
                .emerald(3L)
                .amethyst(4L)
                .build();

        when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
        when(currencyCache.isEnabled()).thenReturn(true);
        when(currencyCache.get(anyString())).thenReturn(Optional.of(currency));

        // Act
        Currency result = currencyService.getCurrency("1");

        // Assert
        assertThat(result).isEqualTo(currency);
    }

    @Test
    void get_currency_on_existing_gamesave_id_when_not_cached() {
        // Arrange
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail("test@test.com")
                .goldAmount(1L)
                .diamondAmount(2L)
                .emeraldAmount(3L)
                .amethystAmount(4L)
                .build();

        Currency currency = Currency.builder()
                .gold(1L)
                .diamond(2L)
                .emerald(3L)
                .amethyst(4L)
                .build();

        when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
        when(currencyCache.isEnabled()).thenReturn(false);
        when(currencyCache.get(anyString())).thenReturn(Optional.empty());

        // Act
        Currency result = currencyService.getCurrency("1");

        // Assert
        assertThat(result).isEqualTo(currency);
    }

    @Test
    void get_currency_on_existing_gamesave_id_when_partially_cached() {
        // Arrange
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail("test@test.com")
                .goldAmount(1L)
                .diamondAmount(2L)
                .emeraldAmount(3L)
                .amethystAmount(4L)
                .build();

        Currency currencyCached = Currency.builder()
                .gold(1L)
                .build();

        Currency currency = Currency.builder()
                .gold(1L)
                .diamond(2L)
                .emerald(3L)
                .amethyst(4L)
                .build();

        when(currencyRepository.findById(anyString())).thenReturn(Optional.of(currencyEntity));
        when(currencyCache.isEnabled()).thenReturn(true);
        when(currencyCache.get(anyString())).thenReturn(Optional.of(currencyCached));

        // Act
        Currency result = currencyService.getCurrency("1");

        // Assert
        assertThat(result).isEqualTo(currency);
    }

    @Test
    void get_currency_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.getCurrency(null));
    }

    @Test
    void save_currency_on_null_game_save_id_with_to_cache_to_true() {
        // Arrange
        Currency currency = new Currency(1L, 2L, 3L, 4L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, true));
    }

    @Test
    void save_currency_on_null_game_save_id_with_to_cache_to_false() {
        // Arrange
        Currency currency = new Currency(1L, 2L, 3L, 4L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency(null, currency, false));
    }

    @Test
    void save_currency_on_null_currency_with_to_cache_to_false() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency("1", null, false));
    }

    @Test
    void save_currency_on_null_currency_with_to_cache_to_true() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency("1", null, true));
    }

    @Test
    void save_currency_where_all_properties_are_null_with_cache_to_true() {
        // Arrange
        Currency currency = new Currency(null, null, null, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency("1", currency, true));
    }

    @Test
    void save_currency_where_all_properties_are_null_with_cache_to_false() {
        // Arrange
        Currency currency = new Currency(null, null, null, null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> currencyService.saveCurrency("1", currency, false));
    }

    @Test
    void save_currency_on_existing_gamesave_with_all_valid_currencies_value() {
        // Arrange
        Currency currency = new Currency(1L, 2L, 3L, 4L);

        // Act + Assert
        assertDoesNotThrow(() -> currencyService.saveCurrency("1", currency, true));
    }
}
