package com.tal.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ʵ���ļ��ϴ����ܵ�servlet
 * 
 * @author �⺣��
 * @d2016��12��16��
 */

public class uploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6495305140683263217L;

	/**
	 * Constructor of the object.
	 */
	public uploadServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// request.setCharacterEncoding("utf-8");
		// response.setHeader("content-type", "text/html;charset=UTF-8");
		// response.setCharacterEncoding("utf-8");

		request.setCharacterEncoding("gbk");
		response.setHeader("content-type", "text/html;charset=gbk");
		response.setCharacterEncoding("gbk");

		// ���������ļ�������ֽ�
		int MAX_SIZE = 102400 * 102400;
		// ������·���ı������
		String rootPath;
		// String filePath = null;
		// �����ļ�������
		DataInputStream in = null;
		FileOutputStream fileOut = null;
		// ȡ�ÿͻ��˵������ַ
		// String remoteAddr = request.getRemoteAddr();
		// ��÷�����������
		String serverName = request.getServerName();

		// ȡ�û���������ľ��Ե�ַ
		String realPath = request.getRealPath(serverName);
		realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
		// �����ļ��ı���Ŀ¼
		rootPath = realPath + "\\WEB-INF\\jmx\\";
		// ȡ�ÿͻ����ϴ�����������
		String contentType = request.getContentType();
		try {
			if (contentType.indexOf("multipart/form-data") >= 0) {
				// �����ϴ�������
				in = new DataInputStream(request.getInputStream());
				int formDataLength = request.getContentLength();
				if (formDataLength > MAX_SIZE) {
					response.getWriter().println(
							"<P>�ϴ����ļ��ֽ��������Գ���" + MAX_SIZE + "</p>");
					return;
				}
				// �����ϴ��ļ�������
				byte dataBytes[] = new byte[formDataLength];
				int byteRead = 0;
				int totalBytesRead = 0;
				// �ϴ������ݱ�����byte����
				while (totalBytesRead < formDataLength) {
					byteRead = in.read(dataBytes, totalBytesRead,
							formDataLength);
					totalBytesRead += byteRead;
				}
				// ����byte���鴴���ַ���
				String file = new String(dataBytes);
				// out.println(file);
				// ȡ���ϴ������ݵ��ļ���
				String saveFile = file
						.substring(file.indexOf("filename=\"") + 10);
				saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
				saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
						saveFile.indexOf("\""));

				System.out.println("�ϴ����ļ�����" + saveFile);
				request.setAttribute("filePath", saveFile);
				// ����ipֵΪ�գ�����
				request.setAttribute("remote_ip1", null);
				request.setAttribute("remote_ip2", null);
				request.setAttribute("remote_ip3", null);
				request.setAttribute("remote_ip4", null);
				// filePath = saveFile;
				int lastIndex = contentType.lastIndexOf("=");
				// ȡ�����ݵķָ��ַ���
				String boundary = contentType.substring(lastIndex + 1,
						contentType.length());
				// ��������·�����ļ���
				String fileName = rootPath + saveFile;
				// out.print(fileName);
				int pos;
				pos = file.indexOf("filename=\"");
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				pos = file.indexOf("\n", pos) + 1;
				int boundaryLocation = file.indexOf(boundary, pos) - 4;
				// ȡ���ļ����ݵĿ�ʼ��λ��
				int startPos = ((file.substring(0, pos)).getBytes()).length;
				// ȡ���ļ����ݵĽ�����λ��
				int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
				// ��������ļ��Ƿ����
				// ��������ļ���Ŀ¼�Ƿ����
				File fileDir = new File(rootPath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// �����ļ���д����
				fileOut = new FileOutputStream(fileName);
				// �����ļ�������
				fileOut.write(dataBytes, startPos, (endPos - startPos));
				fileOut.close();
			} else {
				// String content = request.getContentType();
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		request.getRequestDispatcher("/setParameter.jsp").forward(request,
				response);// ת��
	}
}