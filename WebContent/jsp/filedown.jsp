<%@ page contentType="application; charset=EUC-KR"%><%@ page language="java" import="java.util.*,java.io.*" %><%
String filename     = "";
//String filepath     = "C:\\TEMP\\";
//String filepath     = "/cabisdata/pub/temp/";
String filepath     = "";
String filegubun    = "";

int intTemp = 0;

try{
    filename    = request.getParameter("filename");
    //filename = new String(filename.getBytes("iso8859-1"),"euc-kr");
    filepath   = request.getParameter("filepath");
    
//	filename    = "ct_prgm.XLS";
    filepath = filepath + filename;
    filepath = new String(filepath.getBytes("iso8859-1"),"euc-kr");
//    filepath = java.net.URLEncoder.encode(filepath, "UTF-8");
    File   file = new File(filepath);

byte b[] = new byte[4096];

//response.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
response.setHeader("Content-Disposition", "attachment;filename=\""+ file.getName() + "\"");
    if (file.isFile()){
     BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
     BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
     int read = 0;
     try {
      while ((read = fin.read(b)) != -1){
          outs.write(b,0,read);
      }
      outs.close();
      fin.close();
     } catch (Exception e) {
     } finally {
      if(outs!=null) outs.close();
      if(fin!=null) fin.close();
     }
    }
}catch (Exception e){
}finally{
}
%>