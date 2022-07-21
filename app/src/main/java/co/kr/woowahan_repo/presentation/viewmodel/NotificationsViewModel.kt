package co.kr.woowahan_repo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_repo.di.ServiceLocator
import co.kr.woowahan_repo.domain.model.GithubNotification
import co.kr.woowahan_repo.domain.repository.GithubNotificationsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationsViewModel(
    private val githubNotificationsRepository: GithubNotificationsRepository
) : ViewModel() {
    private var page = 1

    private val _isDataLoading = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> get() = _isDataLoading
    private val _notifications = MutableLiveData<List<GithubNotification>>()
    val notifications: LiveData<List<GithubNotification>> get() = _notifications

    fun fetchNotifications() {
        viewModelScope.launch {
            _isDataLoading.value = true
            githubNotificationsRepository.fetchNotifications(page)
                .onSuccess { _notifications.value = it }
                .onFailure { Timber.e(it) }
                .also { _isDataLoading.value = false }
        }
    }

    fun patchNotificationAsRead(threadId: String, position: Int) {
        viewModelScope.launch {
            _isDataLoading.value = true
            if (githubNotificationsRepository.patchNotificationAsRead(threadId)) {
                _notifications.value =
                    requireNotNull(notifications.value).toMutableList().apply { removeAt(position) }
            } else {
                _notifications.value =
                    requireNotNull(notifications.value).toList().map { it.copy() }
            }.also { _isDataLoading.value = false }
        }
    }
}