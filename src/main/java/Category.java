import redis.clients.jedis.Jedis;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by nitix on 02/10/2016.
 */
public class Category {

    private static String getCategoryName(String category) {
        if(category.startsWith("category:")) {
            return category;
        } else {
            return "category:" + category;
        }
    }

    public static void addToCategory(Jedis conn, String articleId, String category) {
        conn.sadd(articleId + ":categories", category);
        conn.sadd(getCategoryName(category), articleId);
    }

    public static void remFromCategory(Jedis conn, String articleId, String category) {
        conn.srem(articleId+ ":categories", category);
        conn.srem(getCategoryName(category), articleId);
    }

    public static Set<String> getMembersOfCategory(Jedis conn, String category) {
        return conn.smembers(getCategoryName(category));
    }

    public static Set<String> getCategories(Jedis conn, String articleId) {
        return conn.smembers(articleId + ":categories");
    }

    public static Set<Tuple<String, Double>> getArticlesOfCategoryWithScore(Jedis conn, String category) {
        Set<Tuple<String, Double>> set = conn.smembers(getCategoryName(category))
                .stream().map(articleId -> new Tuple<>(articleId, Vote.getScore(conn, articleId)))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return set;
    }
}
