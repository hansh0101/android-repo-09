package co.kr.woowahan_repo.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.woowahan_repo.di.ServiceLocator
import co.kr.woowahan_repo.domain.model.GithubNotification
import co.kr.woowahan_repo.domain.repository.NotificationsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository = ServiceLocator.getNotificationsRepository()
) : ViewModel() {
    private var page = 1

    private val _isDataLoading = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> get() = _isDataLoading
    private val _notifications = MutableLiveData<List<GithubNotification>>()
    val notifications: LiveData<List<GithubNotification>> get() = _notifications

    fun fetchNotifications() {
        viewModelScope.launch {
            _isDataLoading.value = true
            notificationsRepository.fetchNotifications(page)
                .onSuccess {
                    Timber.tag("Notifications Success").d(it.toString())
                    _notifications.value = it
                    page++
                }.onFailure {
                    Timber.tag("Notifications Failure").e(it)
                }.also {
                    _isDataLoading.value = false
                }
        }
    }
}