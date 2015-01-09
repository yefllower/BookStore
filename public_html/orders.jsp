<%@ 
	page language="java" import="bookstore.*" 
	import="javax.servlet.http.Cookie"
%>
<html>
<head>
<script LANGUAGE="javascript">

function check_all_fields(form_obj){
	alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
	if( form_obj.attributeValue.value == ""){
		alert("Search field should be nonempty");
		return false;
	}
	return true;
}

function pass_value(obj){
	return true;
}

</script> 
</head>
<body>

<%
int UID = -1;
int USP = 0;
Cookie cookies[] = request.getCookies();
for(int n = 0; n < cookies.length; n++) {
	Cookie newCookie = cookies[n];
	if (newCookie.getName().equals("BookStoreUID"))
		UID = new Integer(newCookie.getValue());
	if (newCookie.getName().equals("BookStoreUSP"))
		USP = new Integer(newCookie.getValue());
}
//out.println(" uid is " + UID + " usp is " + USP);
String actionName = request.getParameter("actionName");
//out.println(actionName);
if( actionName == null) {
	if (USP == 0) {
%>
	<p>未登录</p>
	<p>你可以</p>
<%
	} else if (USP == 1) {
%>
	<p>已登录</p>
	<p>你可以</p>
<%
	} else if (USP >= 2) {
%>
	<p>已登录,管理员大人好</p>
	<p>您可以</p>
<%
}
%>

<%
	if (USP == 0) {
%>
	<form name="user_register" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="register">
		<input type=submit value="注册">
	</form>
	<form name="user_login" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="login">
		<input type=submit value="登录">
	</form>
<%
	}
%>

<%
	if (USP >= 1) {
%>
	<form name="user_switch" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="login">
		<input type=submit value="切换用户">
	</form>
	<form name="user_logout" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="logout">
		<input type=submit value="注销">
	</form>
<%
	}
%>
	<p>或者</p>
	<form name="browse" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="browse">
		<input type=submit value="找新书">
	</form>
	<form name="useful" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="useful">
		<input type=submit value="看书评">
	</form>
<%
	if (USP >= 1) {
%>
	<form name="neworder" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="neworder">
		<input type=submit value="新订单">
	</form>
	<form name="feedback" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="feedback">
		<input type=submit value="新反馈">
	</form>
	<form name="rating" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="rating">
		<input type=submit value="新评价">
	</form>
	<form name="trust" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="trust">
		<input type=submit value="新关系">
	</form>
	<form name="suggest" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="suggest">
		<input type=submit value="求推荐">
	</form>
<%
	}
%>
	<form name="seperate" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="seperate">
		<input type=submit value="查二度">
	</form>
<%
	if (USP >= 2) {
%>
	<p>或者</p>
	<form name="newbook" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="newbook">
		<input type=submit value="添新书">
	</form>
	<form name="newcopy" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="newcopy">
		<input type=submit value="添旧书">
	</form>
	<form name="stats" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="stats">
		<input type=submit value="看统计">
	</form>
	<form name="award" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="award">
		<input type=submit value="发奖励">
	</form>
<%
	}
} else 
if (actionName.equals("register")) {
%>
	<form name="user_register" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="registerSubmit">
		<table border="0">
			<tr>
				<td>用户名</td>
				<td><input type=text name="username" ></td>
				<td><font color="red">*</font></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input type=password name="password" ></td>
				<td><font color="red">*</font></td>
			</tr>
			<tr>
				<td>姓名</td>
				<td><input type=text name="name" ></td>
			</tr>
			<tr>
				<td>地址</td>
				<td><input type=text name="address" ></td>
			</tr>
			<tr>
				<td>电话</td>
				<td><input type=text name="phone" ></td>
			</tr>
		</table>
		<input type=submit value="注册">
	</form>
<%
} else 
if (actionName.equals("registerSubmit")) {
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String name = request.getParameter("name");
	String address = request.getParameter("address");
	String phonenum = request.getParameter("phone");

	if (!(username != null && username.length() != 0)) {
		%>
			亲你没敲用户名啊...
			<BR>
		<%
	}
	else if (!(password != null && password.length() != 0)) {
		%>
			亲你得设个密码啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.register(username, password, name, address, phonenum);
		%>
			注册成功啦~
			<BR>
			不过想登录还是回首页吧
			<BR>
		<%
	}
}
else
if (actionName.equals("logout")) {
	Cookie cookiess[] = request.getCookies();
	for(int n = 0; n < cookiess.length; n++) {
		if (cookiess[n].getName().equals("BookStoreUID") || cookiess[n].getName().equals("BookStoreUSP")) {
			cookiess[n].setMaxAge(0);
			response.addCookie(cookiess[n]);
		}
	}
	cookiess = request.getCookies();
	%>
		注销成功啦~ 白白
		<BR>
	<%
}
else 
if (actionName.equals("login")) {
%>
	<form name="user_login" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="loginSubmit">
		<table border="0">
			<tr>
				<td>用户名</td>
				<td><input type=text name="username" ></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input type=password name="password" ></td>
			</tr>
		</table>
		<input type=submit value="登录">
	</form>
<%
}
else 
if (actionName.equals("loginSubmit")) {
	String username = request.getParameter("username");
	String password = request.getParameter("password");

	if (!(username != null && username.length() != 0)) {
		%>
			亲你要敲用户名啊...
			<BR>
		<%
	}
	else if (!(password != null && password.length() != 0)) {
		%>
			亲你得输入密码啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		int res = order.login(username, password);
		if (res == -1) {
			%>
				登录失败了...查无此人...
				<BR>
			<%
		} else {
			%>
				登录成功啦~ 请返回首页~
				<BR>
			<%
			Cookie cookiess[] = request.getCookies();
			for(int n = 0; n < cookiess.length; n++) {
				if (cookiess[n].getName().equals("BookStoreUID") || cookiess[n].getName().equals("BookStoreUSP")) {
					cookiess[n].setMaxAge(0);
				}
			}
			Cookie theUIDCookie = new Cookie("BookStoreUID", "" + (res / 10));
			response.addCookie(theUIDCookie);
			Cookie theUSPCookie = new Cookie("BookStoreUSP", "" + (res % 10));
			response.addCookie(theUSPCookie);
		}
	}
}
else 
if (actionName.equals("neworder")) {
%>
	<form name="user_neworder" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="neworderSubmit">
		<table border="0">
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>数量</td>
				<td><input type=text name="number" value="1"></td>
			</tr>
		</table>
		<input type=submit value="下单">
	</form>
<%
}
else 
if (actionName.equals("neworderSubmit")) {
	String isbn = request.getParameter("isbn");
	String numberStr = request.getParameter("number");

	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你要说是哪本书啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.neworder(USP, UID, isbn, numberStr);
		if (res.equals("No such Book")) {
			%>
				下单失败了...查无此书...
				<BR>
			<%
		} else 
		if (res.equals("No enough Book")) {
			%>
				下单失败了...库存不足...
				<BR>
			<%
		} else 
		{
			%>
				下单成功啦~ 
				总价 <%= res %>
				<BR>
				可以剁手了...
				<BR>
			<%
		}
	}
}
else 
if (actionName.equals("newbook")) {
%>
	<form name="user_newbook" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="newbookSubmit">
		<table border="0">
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>标题</td>
				<td><input type=text name="title" ></td>
			</tr>
			<tr>
				<td>作者</td>
				<td><input type=text name="authorString" ></td>
			</tr>
			<tr>
				<td>出版社</td>
				<td><input type=text name="publisher" ></td>
			</tr>
			<tr>
				<td>年份</td>
				<td><input type=text name="yearString" ></td>
			</tr>
			<tr>
				<td>库存</td>
				<td><input type=text name="stockString" ></td>
			</tr>
			<tr>
				<td>价格</td>
				<td><input type=text name="priceString" ></td>
			</tr>
			<tr>
				<td>主题</td>
				<td><input type=text name="subject" ></td>
			</tr>
			<tr>
				<td>
					简装:
					<input type="radio" checked="checked" name="format" value="softcover" />
					精装:
					<input type="radio" name="format" value="hardcover" />
				</td>
			</tr>
		</table>
		<input type=submit value="添加新书">
	</form>
<%
}
else 
if (actionName.equals("newbookSubmit")) {
	String isbn = request.getParameter("isbn");
	String title = request.getParameter("title");
	String authorString = request.getParameter("authorString");
	String publisher = request.getParameter("publisher");
	String yearString = request.getParameter("yearString");
	String stockString = request.getParameter("stockString");
	String priceString = request.getParameter("priceString");
	String subject = request.getParameter("subject");
	String format = request.getParameter("format");
	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你要说是哪本书啊...
			<BR>
		<%
	}
	else
	if (!(title != null && title.length() != 0 && authorString != null && authorString.length() != 0 
		&& publisher != null && publisher.length() != 0 && yearString != null && yearString.length() != 0
		&& stockString != null && stockString.length() != 0 && priceString != null && priceString.length() != 0)) {
		%>
			信息不全,拒绝...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		int res = order.newbook(USP, isbn, title, authorString, publisher, yearString, stockString, priceString, format, subject);
		if (res == -1) {
			%>
				失败了...
				<BR>
			<%
		} else 
		{
			%>
				加书成功啦~ 
				<BR>
			<%
		}
	}
}
else 
if (actionName.equals("newcopy")) {
%>
	<form name="user_newcopy" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="newcopySubmit">
		<table border="0">
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>数量</td>
				<td><input type=text name="deltaString" ></td>
			</tr>
		</table>
		<input type=submit value="添加旧书">
	</form>
<%
}
else 
if (actionName.equals("newcopySubmit")) {
	String isbn = request.getParameter("isbn");
	String deltaString = request.getParameter("deltaString");
	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你要说是哪本书啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		int res = order.newcopy(USP, isbn, deltaString);
		if (res == -1) {
			%>
				失败了...好像没有这本书...
				<BR>
			<%
		} else 
		{
			%>
				加书成功啦~ 
				<BR>
			<%
		}
	}
}

else 
if (actionName.equals("feedback")) {
%>
	<form name="user_feedback" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="feedbackSubmit">
		<table border="0">
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>分数</td>
				<td><input type=text name="scoreString" ></td>
			</tr>
			<tr>
				<td>评价</td>
				<td><input type=text name="comment" ></td>
			</tr>
		</table>
		<input type=submit value="反馈">
	</form>
<%
}
else 
if (actionName.equals("feedbackSubmit")) {
	String isbn = request.getParameter("isbn");
	String scoreString = request.getParameter("scoreString");
	String comment = request.getParameter("comment");
	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你要说是哪本书啊...
			<BR>
		<%
	}
	else if (!(scoreString != null && scoreString.length() != 0)) {
		%>
			亲你要给个分啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		int res = order.feedback(USP, UID, isbn, scoreString, comment);
		if (res == -1) {
			%>
				失败了...好像没有这本书...要不就是分数给的不正常...
				<BR>
			<%
		} else 
		{
			%>
				反馈成功啦~ 
				<BR>
			<%
		}
	}
}
else 
if (actionName.equals("rating")) {
%>
	<form name="user_rating" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="ratingSubmit">
		<table border="0">
			<tr>
				<td>用户名</td>
				<td><input type=text name="username" ></td>
			</tr>
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>分数</td>
				<td><input type=text name="scoreString" ></td>
			</tr>
		</table>
		<input type=submit value="评价">
	</form>
<%
}
else 
if (actionName.equals("ratingSubmit")) {
	String username = request.getParameter("username");
	String isbn = request.getParameter("isbn");
	String scoreString = request.getParameter("scoreString");
	if (!(username != null && username.length() != 0)) {
		%>
			亲你要评价谁啊...
			<BR>
		<%
	}
	else if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你要说是哪本书啊...
			<BR>
		<%
	}
	else if (!(scoreString != null && scoreString.length() != 0)) {
		%>
			亲你要给个分啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.rate(USP, UID, username, isbn, scoreString);
		if (res.equals("Book not Found")) {
			%>
				失败了...好像没有这本书...
				<BR>
			<%
		} else 
		if (res.equals("User not Found")) {
			%>
				失败了...好像没有这人...
				<BR>
			<%
		} else 
		if (res.equals("You are Cheater")) {
			%>
				失败了...你怎么可以评价自己呢...苏格拉底说, 认识你自己...
				<BR>
			<%
		} else 
		{
			%>
				评价成功啦~ 
				<BR>
			<%
		}
	}
}
else 
if (actionName.equals("trust")) {
%>
	<form name="user_trust" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="trustSubmit">
		<table border="0">
			<tr>
				<td>用户名</td>
				<td><input type=text name="username" ></td>
			</tr>
			<tr>
				<td>
					信任:
					<input type="radio" checked="checked" name="trustString" value="trusted" />
					不信任:
					<input type="radio" name="trustString" value="untrusted" />
				</td>
			</tr>
		</table>
		<input type=submit value="信任">
	</form>
<%
}
else 
if (actionName.equals("trustSubmit")) {
	String username = request.getParameter("username");
	String trustString = request.getParameter("trustString");
	if (!(username != null && username.length() != 0)) {
		%>
			亲你要评价谁啊...
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String	res = order.trust(USP, UID, username, trustString);
		if (res.equals("User not Found")) {
			%>
				失败了...好像没有这人...
				<BR>
			<%
		} else 
		{
			if (trustString.length() == 0 || trustString.equals("untrusted")) {
			%>
				已把他加入黑名单... 人与人之间的信任都没有了...
				<BR>
			<%
			} else {
			%>
				已把他加入白名单~ 
				<BR>
			<%
			}
		}
	}
}
else 
if (actionName.equals("browse")) {
%>
	<form name="user_browse" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="browseSubmit">
		<table>
			<tr>
				<td>书名关键字</td>
				<td><input type=text name="title" ></td>
			</tr>
			<tr>
				<td>出版社</td>
				<td><input type=text name="publisher" ></td>
			</tr>
			<tr>
				<td>作者</td>
				<td><input type=text name="author" ></td>
			</tr>
			<tr>
				<td>主题</td>
				<td><input type=text name="subject" ></td>
			</tr>
			<tr>
				<td>排序按照</td>
					
				<td>
					年份
					<input type="radio" checked="checked" name="listType" value="year" />
					反馈
					<input type="radio" name="listType" value="feedback" />
					信任
					<input type="radio" name="listType" value="trusted" />
				</td>
				</table>
			</tr>
			<tr>
				<td>
					降序
					<input type="radio" checked="checked" name="orderType" value="DESC" />
					升序
					<input type="radio" name="orderType" value="INC" />
				</td>
			</tr>
		</table>
		<BR>
		<input type=submit value="找书">
	</form>
<%
}
else 
if (actionName.equals("browseSubmit")) {
	String title = request.getParameter("title");
	String publisher = request.getParameter("publisher");
	String author = request.getParameter("author");
	String subject = request.getParameter("subject");
	String listType = request.getParameter("listType");
	String orderType = request.getParameter("orderType");
	{
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.browse(USP, UID, title, publisher, author, subject, listType, orderType);
		if (res.equals("Error")) {
			%>
				拜托...要登录才能知道你信任谁啊...
				<BR>
			<%
		} else 
		{
			out.println(res + "<BR>");
		}
	}
}
else 
if (actionName.equals("useful")) {
%>
	<form name="user_userful" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="usefulSubmit">
		<table border="0">
			<tr>
				<td>ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
			<tr>
				<td>显示条数</td>
				<td><input type=text name="numberString" ></td>
			</tr>
		</table>
		<input type=submit value="查看">
	</form>
<%
}
else 
if (actionName.equals("usefulSubmit")) {
	String isbn = request.getParameter("isbn");
	String numberString = request.getParameter("numberString");
	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你到底想问哪本书的反馈啊...("#)
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.useful(USP, UID, isbn, numberString);
		{
			out.println(res + "<BR>");
		}
	}
}

else 
if (actionName.equals("suggest")) {
%>
	<form name="user_suggest" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="suggestSubmit">
		<table border="0">
			<tr>
				<td>相关书的ISBN</td>
				<td><input type=text name="isbn" ></td>
			</tr>
		</table>
		<input type=submit value="查看">
	</form>
<%
}
else 
if (actionName.equals("suggestSubmit")) {
	String isbn = request.getParameter("isbn");
	if (!(isbn != null && isbn.length() != 0)) {
		%>
			亲你没买书我怎么推荐啊...("#)
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String	res = order.suggest(USP, UID, isbn);
		{
			out.println(res + "<BR>");
		}
	}
}
else 
if (actionName.equals("seperate")) {
%>
	<form name="user_seperate" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="seperateSubmit">
		<table border="0">
			<tr>
				<td>第一个名字</td>
				<td><input type=text name="name1" ></td>
			</tr>
			<tr>
				<td>第二个名字</td>
				<td><input type=text name="name2" ></td>
			</tr>
		</table>
		<input type=submit value="查看">
	</form>
<%
}
else 
if (actionName.equals("seperateSubmit")) {
	String name1 = request.getParameter("name1");
	String name2 = request.getParameter("name2");
	if (!(name1 != null && name2.length() != 0 && name2 != null && name1.length() != 0)) {
		%>
			亲你想看哪两个人啊...("#)
			<BR>
		<%
	}
	else {
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String	res = order.seperate(USP, UID, name1, name2);
		{
			out.println(res + "<BR>");
		}
	}
}
else 
if (actionName.equals("stats")) {
%>
	<form name="user_stats" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="statsSubmit">
		<table border="0">
			<tr>
				<td>显示条数</td>
				<td><input type=text name="numberString" ></td>
			</tr>
		</table>
		<input type=submit value="查看">
	</form>
<%
}
else 
if (actionName.equals("statsSubmit")) {
	String numberString = request.getParameter("numberString");
	{
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.statistics(USP, UID, numberString);
		{
			out.println(res + "<BR>");
		}
	}
}
else 
if (actionName.equals("award")) {
%>
	<form name="user_award" method=get onsubmit="return pass_value(this)" action="orders.jsp">
		<input type=hidden name="actionName" value="awardSubmit">
		<table border="0">
			<tr>
				<td>显示条数</td>
				<td><input type=text name="numberString" ></td>
			</tr>
		</table>
		<input type=submit value="查看">
	</form>
<%
}
else 
if (actionName.equals("awardSubmit")) {
	String numberString = request.getParameter("numberString");
	{
		bookstore.Connector connector = new Connector();
		bookstore.HtmlOrder order = new HtmlOrder(connector.con);
		String res = order.award(USP, UID, numberString);
		{
			out.println(res + "<BR>");
		}
	}
}
else
{
	out.println("Unknown action '" + actionName + "'" + "<BR>");
	
  %>

<%
// connector.closeStatement();
 //connector.closeConnection();
}  // We are ending the braces for else here
%>

<a href="orders.jsp"> 返回首页 </a></p>

</body>
