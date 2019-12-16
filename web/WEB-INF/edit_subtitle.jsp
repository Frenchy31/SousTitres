<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Editer les sous-titres</title>
</head>
<nav><button><a href="list">Retour</a></button></nav>
<body>
	<h1>${ titreVideo }.</h1>
    <form method="post" action="edit">
	    <table>
			<c:if test="${!empty paragraphes}">
				<tr>
					<th>Texte initial</th>
					<th>Traduction</th>
				</tr>
				<c:forEach items="${ paragraphes }" var="paragraphe" varStatus="status">
					<tr>
						<td style="text-align:right;" id="${ paragraphe.getNumParagraphe() }" name="${ paragraphe.getNumParagraphe() }" ><c:out value="${ paragraphe.getTexteAffiche() }" /></td>
						<td><input type="text" name="line${ paragraphe.getNumParagraphe() }" id="line${ paragraphe.getNumParagraphe() }"
								<c:if test="${!empty paragraphesTraduits }"> value="${ paragraphesTraduits.get(status.index).getTexteAffiche() }" </c:if> size="35" /></td>
						<td><input style="display: none" type="text" name="tpsDebut${ paragraphe.getNumParagraphe() }" value="${ paragraphe.getTempsDebut() }"></td>
						<td><input style="display: none" type="text" name="tpsFin${ paragraphe.getNumParagraphe() }" value="${ paragraphe.getTempsFin() }"></td>
					</tr>
				</c:forEach>
			</c:if>
	    </table>
		<button style="align-self: center" type="submit">Save</button>
		<select name="langueTrad">
			<c:forEach items="${ locales }" var="locale">
			<option value="${ fun:substring(locale,0,2)}"><c:out value="${ locale }"/></option>
			</c:forEach>
		</select>
		<input hidden name="idVideo" value="${idVideo}">
		<input hidden name="langue" value="${langue}">
    </form>
</body>
</html>