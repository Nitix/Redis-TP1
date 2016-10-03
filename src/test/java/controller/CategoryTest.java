package controller;

import controller.Article;
import controller.Category;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nitix on 02/10/2016.
 */
public class CategoryTest {

    private static final String host = "localhost";
    private static final int port = 6379;
    private static final int db = 2;

    private static String articleId;

    @BeforeClass
    public static void cleanUp(){
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        if (db != 0){
            jedis.flushDB();
        }
        articleId = Article.ajoutArticle(jedis, "nitix", "Javaaaa", "lien");
    }

    @Test
    public void addAndGetCategoryTest() {
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        String category = "Java";
        Category.addToCategory(jedis, articleId, category);
        assertEquals(1, Category.getMembersOfCategory(jedis, category).size());
        assertEquals(1, Category.getCategories(jedis, articleId).size());
    }

    @Test
    public void remCategoryTest() {
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        String category = "Java";
        Category.addToCategory(jedis, articleId, category);
        Category.remFromCategory(jedis, articleId, category);
        assertEquals(0, Category.getMembersOfCategory(jedis, category).size());
        assertEquals(0, Category.getCategories(jedis, articleId).size());
    }

    @Test
    public void getArticleWithScore() {
        Jedis jedis = new Jedis(host, port);
        jedis.select(db);
        String category = "Java";
        Category.addToCategory(jedis, articleId, category);
        Set<Tuple<String, Double>> articles = Category.getArticlesOfCategoryWithScore(jedis, category);
        assertEquals(1, articles.size());
        Tuple<String, Double> article = articles.iterator().next();
        assertEquals(articleId, article.first);
        assertEquals(Vote.getScore(jedis, articleId), article.second, 1);
    }
}
