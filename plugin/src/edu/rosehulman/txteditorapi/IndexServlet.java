package edu.rosehulman.txteditorapi;

import protocol.HttpRequest;
import protocol.response.HttpResponseDecorator;
import servlet.Servlet;

public class IndexServlet implements Servlet {

	private HttpRequest req;
	private HttpResponseDecorator dec;
	
	@Override
	public void set(HttpRequest req, HttpResponseDecorator dec) {
		this.req = req;
		this.dec = dec;
	}

	@Override
	public void run() {
		dec.write("<!DOCTYPE html>");
        dec.write("<html>");
        dec.write("<head>");
        dec.write("<script src=\"//code.jquery.com/jquery-1.11.3.min.js\"></script>");   
        dec.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\">");
        dec.write("<title>TXT Editor</title>");            
        dec.write("</head>");
        dec.write("<body>");
        dec.write("<nav class=\"navbar navbar-default\">"
        		+ "<div class=\"container-fluid\">"
        		+ "<div class=\"navbar-header\">"
        		+ "<a class=\"navbar-brand\" href=\"#\">"
        		+ "TXT Editor"
        		+ "</a>"
        		+ "</div>"
        		+ "</div>"
        		+ "</nav>");
        dec.write("<div class='col-md-3 filelist'>"
        		+ "<div class='input-group'>"
        		+ "<input type='txet' placeholder='File Name' class='form-control newFile'/>"
        		+ "<span class='input-group-btn'>");
        dec.write("<button class= 'btn btn-primary' onclick=\"createFile()\" >Create</button>"
        		+ "</span>"
        		+ "</div>");
        dec.write("<button class= 'btn btn-primary' onclick=\"deleteFile()\" >Delete</button>");
        dec.write("</div>");
        dec.write("<div class='col-md-9 edit'>");
        dec.write("<h2>Click on File Name to Edit</h2>");
        dec.write("</div>");
        dec.write("<script>"
        		+ "var curFile;"
        		+ "getAll();"
        		+ "function createFile() {"
        		+ "var name = $('.newFile').val();"
        		+ "$.ajax({"
        		+ "url:'/txteditor/edit',"
        		+ "type:'PUT',"
        		+ "data:JSON.stringify({filename:name}),"
        		+ "contentType:'application/json',"
        		+ "success: function(data) {"
        		+ "data = JSON.parse(data);"
        		+ "if(data.code == 200){"
        		+ "$('.newfile').val('');"
        		+ "getAll();"
        		+ "$('.edit').prepend('<div class=\"alert alert-success\">Create Success</div>');"
        		+ "setTimeout(function(){$('.alert').fadeOut();}, 1500)"
        		+ "}"
        		+ "}"
        		+ "})"
        		+ "}"
        		+"function deleteFile(){"
        		+ "$.ajax({"
        		+ "url:'/txteditor/delete/'+curFile,"
        		+ "type:'DELETE',"
        		+ "success: function(data){"
        		+ "data = JSON.parse(data);"
        		+ "if(data.code == 200){"
        		+ "getAll();"
        		+ "$('.edit').prepend('<div class=\"alert alert-success\">Delete Success</div>');"
        		+ "setTimeout(function(){$('.alert').fadeOut();}, 1500);"
        		+ "$('.edit').html('<h2>Click on File Name to Edit</h2>')"
        		+ "}"
        		+ "}"
        		+ "})"
        		+ "}"
        		+ "function getAll(){"
        		+ "$.ajax({"
        		+ "url:'/txteditor/getall',"
        		+ "type:'GET',"
        		+ "success: function(data) {"
        		+ "data = JSON.parse(data);"
        		+ "$('.fname').remove();"
        		+ "for(var i = 0; i < data.payload.length; i++){"
        		+ "$('.filelist').append('<div class=\"fname\">'+data.payload[i]+'</div>')"
        		+ "}"
        		+ "bindFname();"
        		+ "}"
        		+ "});"
        		+ "};"
        		+ "function bindFname() {"
        		+ "$('.fname').click(function(){"
        		+ "curFile = $(this).html();"
        		+ "$.ajax({"
        		+ "url:'/txteditor/get/'+curFile,"
        		+ "type:'GET',"
        		+ "success: function(data){"
        		+ "console.log(data);"
        		+ "data = JSON.parse(data);"
        		+ "$('.edit').html('');"
        		+ "$('.edit').append('<h2 class=\"edit-name\">'+data.payload.filename+'</h2>');"
        		+ "$('.edit').append('<textarea class=\"edit-content\" rows=\"20\" cols=\"150\">' + data.payload.content + '</textarea><br/>');"
        		+ "$('.edit').append('<div class=\"btn btn-primary edit-btn\">Save</div>');"
        		+ "bindEdit();"
        		+ "}"
        		+ "})"
        		+ "});"
        		+ "};"
        		+ "function bindEdit() {"
        		+ "$('.edit-btn').click(function(){"
        		+ "var name = $('.edit-name').html();"
        		+ "var content=$('.edit-content').val();"
        		+ "$.ajax({"
        		+ "url:'/txteditor/edit',"
        		+ "type:'POST',"
        		+ "data:JSON.stringify({filename:name, content:content}),"
        		+ "contentType:'application/json',"
        		+ "success: function(data) {"
        		+ "data = JSON.parse(data);"
        		+ "if(data.code == 200){"
        		+ "$('.edit').prepend('<div class=\"alert alert-success\">Save Success</div>');"
        		+ "setTimeout(function(){$('.alert').fadeOut();}, 1500)"
        		+ "}"
        		+ "}"
        		+ "})"
        		+ "});"
        		+ "};"
        		
        		+ "</script>");

        dec.write("</body>");
        dec.write("</html>");
	}

}
