package chemaclasskt.demo

import chemaclasskt.demo.model.Article
import chemaclasskt.demo.repository.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController(val repository: ArticleRepository) {

    @GetMapping("/articles")
    fun getAllArticles(): List<Article> = repository.findAll()

    @PostMapping("/articles")
    fun createNewArticle(@Valid @RequestBody article: Article): Article =
            repository.save(article)

    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Article> {
        return repository.findById(articleId).map { article ->
            ResponseEntity.ok(article)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/articles/{id}")
    fun updateArticleById(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {
        return repository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle
                    .copy(title = newArticle.title, content = newArticle.content)
            ResponseEntity.ok().body(repository.save(updatedArticle))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void> {
        return repository.findById(articleId).map { article ->
            repository.delete(article)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}
