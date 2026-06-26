package com.project.compose.core.common.base

import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.common.utils.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private lateinit var viewModel: BaseViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BaseViewModel()
        viewModel.dispatcher = testDispatcher
    }

    @Test
    fun `collectApiAsUiState should allow concurrent API calls`() = runTest {
        val states1 = mutableListOf<UiState<String>>()
        val states2 = mutableListOf<UiState<String>>()

        val flow1 = flow {
            emit(ApiState.Loading)
            delay(100)
            emit(ApiState.Success("Result 1"))
        }

        val flow2 = flow {
            emit(ApiState.Loading)
            delay(200)
            emit(ApiState.Success("Result 2"))
        }

        viewModel.collectApiAsUiState(flow1, resetState = false) { states1.add(it) }
        viewModel.collectApiAsUiState(flow2, resetState = false) { states2.add(it) }

        advanceTimeBy(300)

        assertEquals(2, states1.size)
        assertEquals(UiState.StateLoading, states1[0])
        assertEquals(UiState.StateSuccess("Result 1"), states1[1])

        assertEquals(2, states2.size)
        assertEquals(UiState.StateLoading, states2[0])
        assertEquals(UiState.StateSuccess("Result 2"), states2[1])
    }
}
