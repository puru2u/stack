package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    val snackbar: LiveData<String?>
        get() = _snackbar
    private val _snackbar = MutableLiveData<String?>()

    protected fun launchRequest(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _refreshing.value = true
                _snackbar.value = null
                block()
            } catch (exception: Exception) {
                Timber.e(exception)
                _snackbar.value = "Network error"
            } finally {
                _refreshing.value = false
            }
        }
    }
}
