package controller;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by nitix on 29/09/2016.
 */
public class Article {

    public static String ajoutArticle(Jedis conn, String utilisateur, String titre, String url) {
        String articleId = String.valueOf(conn.incr("article:"));
        long now = System.currentTimeMillis() / 1000;
        String article = "article:" + articleId;
        HashMap<String,String> donnees = new HashMap<>();
        donnees.put("titre", titre);
        donnees.put("lien", url);
        donnees.put("utilisateur", utilisateur);
        donnees.put("timestamp", String.valueOf(now));
        donnees.put("nbvotes", "1");
        conn.hmset(article, donnees);
        conn.zadd("date:article", now, article);
        Vote.initVote(conn, utilisateur, articleId, now );
        return articleId;
    }

    public static Set<String> getArticles(Jedis conn) {
        return conn.zrange("date:article", 0, -1);
    }
}
