Introduction
==================

Ce tp est fait à l'aide de maven et de Java 8.
Les dépendences de maven sont JUnit pour les tests unitaires, et Jedis pour le client redis.

Structure
--------------------

1. Hash Set

Celle-ci permet d'accèder très rapidement à l'article

2. Sorted Set

Les sorted set excèlent avec le fonctionne de score, ou de date.
Celles-ci permetttent de récupérer des N derniers éléments par exemple.

3. Set

La structure du set est appropriée, nous n'avons pas besoin de les garder triée.


Implementation
-------------------

1. Récupération des articles.


La méthode `Article.getArticles(Jedis connection)` retourne la liste de tous les articles.
Cependant son utilisation n'est pas recommandé si beaucoup d'articles sont présents.

2. Augmentation du vote
 
La méthode `Vote.joutVote(Jedis conn, String utilisateur, String articleId, boolean plus)` ajoute un vote, la valeur positive
ou négative est indiquée grâce au boolean `plus`. 

3. Récupération des 10 derniers articles avec le plus grand score

La méthode `Vote.getTredingArticles(Jedis conn)` retourne la liste des Id des 10 derniers articles ayant le plus score.


4. Catégories

Les catégories sont gérés dans la classe `Category`.
La récupéation des articles avec leur score pour une catégorie donnée est de complexité `O(2N)` où N est le nombre 
d'articles dans la catégorie.

Cette opération s'effectue en deux étapes, la première récupération des éléments de la catégorie, puis la récupération
du score pour chaque élément. Cette implémentation pourra être optimisée lors de l'implémentation de la méthode `zmscore` [See antirez/redis#2344](https://github.com/antirez/redis/issues/2344).


 
