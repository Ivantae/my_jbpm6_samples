<%@ page import="java.util.List" %>
<%
String userName = (String) session.getAttribute("currentUser");
List<String> roles = (List<String>) session.getAttribute("roles");
StringBuffer roleStr = new StringBuffer();
if(null != roles){
	for(int i=0; i<roles.size(); i++){
		if(i > 0){
			roleStr.append(",");
		}
		roleStr.append(roles.get(i));
	}
}
%>
Logged In User: <%=userName %> (Owned role: <%=roleStr %>)