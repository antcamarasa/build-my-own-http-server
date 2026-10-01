# build-my-owm-API

This is a living repository i use to deeply learn backend.

0 | Fondations | Network - stream & buffer
  -> Le modèle Client / Serveur | requête / réponse, sans état (stateless) · ○ couches TCP / IP / HTTP
  -> Socket & flux | ★ ServerSocket, Socket, accept · ★ flux d'octets ★ Buffer (position, limite) · ○ octet signé ou non signé
  -> Encodage

 1 | Build my own : HttpServer
  -> HTTP Streams     | lire un flux d'octets au fil de l'arrivée · ★ une requête peut arriver en plusieurs morceaux
  -> TCP              | fiabilité, ordre garanti · ○ comparaison avec UDP · ○ poignée de main, ports
  -> Requests         | structure : request line, headers, ligne vide, body · ★ CRLF
  -> Request Lines    | méthode, cible, version · ★ refuser une ligne malformée → 400 · ○ séparer chemin et query string
  -> HTTP Headers     | nom insensible à la casse · ★ header répété (plusieurs valeurs) · ○ espaces autour de la valeur · ○ taille maximale → 431
  -> HTTP Body        | Content-Length, lecture par compte · ★ corps absent (GET) · ○ taille maximale → 413
  -> HTTP Responses   | (la réponse n'est pas encore écrite sur la socket) ligne de statut, codes et familles (2xx, 4xx, 5xx) · ★ Content-Length en octets · ★ écrire puis fermer · ○ Content-Type, charset
  -> Connexions.      | un client lent ne doit pas bloquer les autres : un thread par connexion, puis un pool · ○ keep-alive (plusieurs requêtes par connexion) · ○ timeouts de lecture
  -> Chunked Encoding | Transfer-Encoding: chunked · ○ envoyer une réponse dont on ne connaît pas la taille · ◇ trailers
  -> Binary Data      | servir un fichier (image) · ○ Content-Type binaire · ◇ HTTP/2 et HTTP/3 : ce qui change
