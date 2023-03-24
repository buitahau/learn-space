package hau.kute.dojo.controller;

import hau.kute.dojo.domain.Article;
import hau.kute.dojo.domain.Author;
import hau.kute.dojo.repository.ArticleRepository;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/create")
    public Article create() {
        Article article = new Article("Reactive Spring");
        article.setAuthors(asList(new Author("Da Vinci"), new Author("Robinson Paul")));
        articleRepository.save(article);
        return article;
    }

    @GetMapping("/author/{authorName}")
    public Page<Article> articleByAuthorName(@PathVariable("authorName") String authorName) {
        return articleRepository.findByAuthorsName(authorName, PageRequest.of(0, 10));
    }

    @GetMapping("/author/custom-query/{authorName}")
    public Page<Article> articleByAuthorNameUsingCustomQuery(@PathVariable("authorName") String authorName) {
        return articleRepository.findByAuthorsNameUsingCustomQuery(authorName, PageRequest.of(0, 10));
    }

    @GetMapping("/title/{title}")
    public Page<Article> articleByTitle(@PathVariable("title") String title) {
        return articleRepository.findByTitle(title, PageRequest.of(0, 10));
    }

    @GetMapping()
    public Page<Article> getAll() {
        return articleRepository.findAll(PageRequest.of(0, 9999));
    }

    @GetMapping("/author/native-query/{authorName}")
    public List<Article> articleByAuthorNameUsingNativeQuery(@PathVariable("authorName") String authorName) {
        Query searchQuery = new NativeSearchQueryBuilder().withFilter(fuzzyQuery("authors.name", authorName)).build();
        SearchHits<Article> articles = elasticsearchOperations.search(searchQuery, Article.class);
        return articles.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @GetMapping("/multi-match/{keyword}")
    public List<Article> searchMultiMatchQuery(@PathVariable("keyword") String keyword) {
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        multiMatchQuery(keyword)
                                .field("title")
                                .field("authors.name")
                                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .build();
        SearchHits<Article> artiles = elasticsearchOperations.search(searchQuery, Article.class);
        return artiles.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @GetMapping("/aggregate-by-operation/group-by-title/{keyword}")
    public Map aggregateGroupByTitle(@PathVariable("keyword") String keyword) {
        String aggregationName = "group_by_title";
        TermsAggregationBuilder aggregationBuilders =
                AggregationBuilders.terms(aggregationName).field("title.keyword");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        multiMatchQuery(keyword)
                                .field("title")
//                                .field("authors.name")
                                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .withAggregations(aggregationBuilders)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        List<Terms.Bucket> buckets = getBuckets(searchHits, aggregationName);
        Map<String, Object> result = new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            result.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return result;
    }

    @GetMapping("/aggregate-by-operation/count-by-title/{keyword}")
    public Map aggregateCountByTitle(@PathVariable("keyword") String keyword) {
        String aggregationName = "group_by_title";
        TermsAggregationBuilder aggregationBuilders =
                AggregationBuilders.terms(aggregationName).field("title.keyword");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        multiMatchQuery(keyword)
                                .field("title")
                                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .withAggregations(aggregationBuilders)
                .build();

        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class);
        List<Terms.Bucket> buckets = getBuckets(searchHits, aggregationName);
        Map<String, Object> result = new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            result.put(bucket.getKeyAsString(), bucket.getDocCount());
        }
        return result;
    }

    private List<Terms.Bucket> getBuckets(SearchHits searchHits, String aggregationName) {
        Terms terms = getTerm(searchHits, aggregationName);
        return (List<Terms.Bucket>) terms.getBuckets();
    }

    private Terms getTerm(SearchHits searchHits, String aggregationName) {
        Aggregations aggregations = getAggregations(searchHits);
        for (Aggregation aggregation : aggregations) {
            Terms terms = (Terms) aggregation;
            if (aggregationName.equals(terms.getName())) {
                return terms;
            }
        }
        throw new RuntimeException("Not found term with aggregation name " + aggregations);
    }

    private Aggregations getAggregations(SearchHits searchHits) {
        ElasticsearchAggregations elasticsearchAggregations = getElasticsearchAggregations(searchHits);
        return elasticsearchAggregations.aggregations();
    }

    private ElasticsearchAggregations getElasticsearchAggregations(SearchHits searchHits) {
        return (ElasticsearchAggregations) searchHits.getAggregations();
    }
}
