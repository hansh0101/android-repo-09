package co.kr.woowahan_repo.domain.entity

data class GithubNotification(
    val repositoryName: String,
    val number: String,
    val updatedAt: String,
    val repositoryImage: String,
    val title: String,
    val comments: String
)
