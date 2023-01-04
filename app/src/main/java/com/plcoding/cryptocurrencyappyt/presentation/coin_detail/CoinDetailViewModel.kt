package com.plcoding.cryptocurrencyappyt.presentation.coin_detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptocurrencyappyt.common.Constants
import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.domain.use_case.get_coin.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state = mutableStateOf(CoinDetailState())
        private set

    init {
        savedStateHandle.get<String>(Constants.PARAMS_COIN_ID)?.let {
            getCoin(it)
        }
    }

    private fun getCoin(coinId: String) {
        getCoinUseCase(coinId = coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    state.value = CoinDetailState(
                        coin = result.data
                    )
                }
                is Resource.Error -> {
                    state.value = CoinDetailState(
                        error = result.message ?: "An unexpected error ocurred"
                    )
                }
                is Resource.Loading -> {
                    state.value = CoinDetailState(
                        isLoading = true
                    )
                }

            }
        }.launchIn(viewModelScope)
    }
}