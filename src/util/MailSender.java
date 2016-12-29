package com.cbscap.util;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * msgmultisendsample creates a simple multipart/mixed message and sends
 Appendix B: Examples Using the JavaMail API 79
 Example: Sending a Message
 JavaMail™ API Design Specification September 2000
 * it. Both body parts are text/plain.
 *
 * usage:java msgmultisendsample to from smtp true|false
 * where to and from are the destination and
 * origin email addresses, respectively, and smtp
 * is the hostname of the machine that has smtp server
 * running. The last parameter either turns on or turns off
 * debugging during sending.
 */

public class MailSender
{
  private boolean debug = true;

  //SMTP Server 관련 환경
  private String smtpHost;

  //Email전송 관련 환경
  private String senderemail; //보내는 사람
  private String rcvemail; //받는 사람
  private String cc; //참조
  private String bcc; //숨은 참조
  private String title; //제목
  private String contents; //내용
  private String filename; //파일이름
  private int filelength; //파일 길이
  private String fullpath; //파일의 Full Path
  private File f = null; //첨부파일을 메일에 첨부하기 위한 파일 객체

  private Thread m_thread;

  public MailSender(String senderemail, String rcvemail, String cc,
                            String bcc, String title, String contents,
                            String filename, String fullpath)
  {
    this.senderemail = senderemail;
    this.rcvemail = rcvemail;
    this.cc = cc;
    this.bcc = bcc;
    this.title = title;
    this.contents = contents;
    this.filename = filename;
    this.fullpath = fullpath;
  }

  public boolean send()
  {
    boolean MailStatus = false;

    MimeBodyPart mbp2 = null;
    FileDataSource fds = null;

    if (debug)
      System.out.println("## File Name : " + fullpath);

    if (rcvemail == null)
      throw new NullPointerException("Email address can't be null.");

    if (title == null)
      title = "";

    if (contents == null)
      contents = "";

    if (filename != null && filename.length() > 0)
    {
      //파일크기를 구한다.
      try
      {
        f = new File(fullpath);
        filelength = Integer.parseInt("" + f.length());
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    // create some properties and get the default Session
    Properties props = new Properties();
    props.put("mail.smtp.host", "mail.wooricap.com");
    //props.put("mail.smtp.host", "ip");

    Session session = null;

    session = Session.getDefaultInstance(props, null);
    session.setDebug(debug);

    try
    {
      // create a message
      MimeMessage msg = new MimeMessage(session);

      senderemail = new String(senderemail.getBytes("KSC5601"), "8859_1");
      rcvemail = new String(rcvemail.getBytes("KSC5601"), "8859_1");
      cc = (cc!=null ? new String( cc.getBytes("KSC5601"),"8859_1") : cc);
      bcc = (bcc!=null ? new String( bcc.getBytes("KSC5601"),"8859_1") : bcc);
      filename = (filename!=null ? new String( filename.getBytes("KSC5601"),"8859_1") : filename);

      msg.setFrom(new InternetAddress(senderemail));
      InternetAddress[] address = makeRecipients(rcvemail);

      msg.setRecipients(Message.RecipientType.TO, address);
      if (cc != null)
      {
        InternetAddress[] addrcc = makeRecipients(cc);
        msg.setRecipients(Message.RecipientType.CC, addrcc);
      }

      if (bcc != null)
      {
        InternetAddress[] addrbcc = makeRecipients(bcc);
        msg.setRecipients(Message.RecipientType.BCC, addrbcc);
      }
      msg.setSubject(MimeUtility.encodeText(title,"KSC5601","B"));
      msg.setSentDate(new Date());

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      /*if(mailformat.equalsIgnoreCase("HTML"))
       mbp1.setContent(contents, "text/html; charset=euc-kr");
          else*/
      mbp1.setContent(contents, "text/html; charset=euc-kr");

      if (filename != null && filename.length() > 0)
      {
        // create the second message part
        mbp2 = new MimeBodyPart();
        // attach the file to the message
        fds = new FileDataSource(fullpath);
        mbp2.setDataHandler(new DataHandler(fds));
        //mbp2.setFileName(fds.getName());
        mbp2.setFileName(filename);
      }

      // create the Multipart and its parts to it
      Multipart mp = new MimeMultipart();
      //80 Appendix B: Examples Using the JavaMail API
      //Example: Sending a Message
      mp.addBodyPart(mbp1);
      if (filename != null && filename.length() > 0)
        mp.addBodyPart(mbp2);

      // add the Multipart to the message
      msg.setContent(mp);
      // send the message
      Transport.send(msg);
      //Transport.close();

      MailStatus = true;

      // new FileUtility(base, sessionid+"_"+filename).delete();
    }
    catch (MessagingException mex)
    {
      Exception ex = null;
      if ( (ex = mex.getNextException()) != null)
      {
        ex.printStackTrace();
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    finally
    {
      //Do Something
    }
    return MailStatus;
  }

  private InternetAddress[] makeRecipients(String addrs) throws
      AddressException
  {
    StringTokenizer toker;
    String delim = "";
    InternetAddress[] addr = null;

    if (addrs != null)
    {
      if (addrs.indexOf(",") != -1)
      {
        delim = ",";
      }
      else if (addrs.indexOf(";") != -1)
      {
        delim = ";";
      }
      else if (addrs.indexOf(":") != -1)
      {
        delim = ":";
      }

      toker = new StringTokenizer(addrs, delim);
      int count = toker.countTokens();
      addr = new InternetAddress[count];
      int i = 0;
      while (toker.hasMoreTokens())
      {
        addr[i++] = new InternetAddress(toker.nextToken().trim());
      }
    }
    return addr;
  }
}

