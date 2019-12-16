<%--
  Created by IntelliJ IDEA.
  User: Sanchez Mathieu
  Date: 06/12/2019
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<html>
  <head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script>
      function telechargeFichier(idVideo) {
        var select = document.getElementById("langue"+idVideo);
        var langue = select.options[select.selectedIndex].value;
        location.href ="/SousTitres_war_exploded/download?idVideo=" + idVideo + "&langue=" + langue;
      }
    </script>
    <title>Sous Titres</title>
  </head>
  <body>
  <c:if test="${ !empty erreur }"><p style="color: red"><c:out value="${ erreur}"/></p></c:if>
  <c:choose>
    <c:when test="${ !empty videos }">
    <ul>
    <c:forEach items="${videos}" var="video">
      <form method="get" action="edit">
      <li><p><c:out value="${video.nom}" /> : <c:out value="${ video.getSousTitres().size() }"/> sous titres disponibles.</p><input hidden value="${ video.getId() }" name="idVideo"/>
        <table>
          <tr>
            <th>Langue initiale</th>
            <th>Langue de traduction</th>
          </tr>
          <tr>
            <td>
              <select name="langue" id="langue${ video.getId() }">
                <c:forEach items="${video.getSousTitres() }" var="sousTitre" >
                  <option><c:out value="${ sousTitre.getLocale()}"/></option>
                </c:forEach>
              </select>
            </td>
            <td>
              <select name="langueTrad">
                <c:forEach items="${ locales }" var="locale" >
                  <option value="${ fun:substring(locale,0,2)}"><c:out value="${ locale }"/></option>
                </c:forEach>
              </select>
            </td>
            <td>
              <button type="submit" value="Choisir">Choisir</button>
            </td>
            <td>
              <button onclick="telechargeFichier(${ video.getId() })" value="Télécharger" type="button">Télécharger</button>
            </td>
          </tr>
        </table></li>
      </form>
    </c:forEach>
    </ul>
    </c:when>
    <c:when test="${ empty videos}"><p>Pas de vidéos disponibles.</p></c:when>
  </c:choose>
  </form>
  <form method="post" action="list" enctype="multipart/form-data">
    <p>
      <label for="description">Titre de la video : </label>
      <input type="text" name="description" id="description" />
    </p>
    <select name="langue">
      <c:forEach items="${ locales }" var="locale" >
        <option value="${ fun:substring(locale,0,2)}"><c:out value="${ locale }"/></option>
      </c:forEach>
    </select>
    <p>
      <label for="fichier">Fichier à envoyer : </label>
      <input type="file" name="fichier" id="fichier" />
    </p>
    <button type="submit" value="Envoyer">Envoyer</button>
  </form>
  </body>
</html>
