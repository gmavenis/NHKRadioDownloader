package com.watchan.nhkradiodownloader;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * @author watadashohei
 *
 */
class Kouza implements Serializable {

	private String title;
	private String hdate;
	private String filename;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHdate() {
		return hdate;
	}

	public void setHdate(String hdate) {
		this.hdate = hdate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}


/**
 * @author watadashohei
 *
 */

public class NHKRadio implements Serializable {

	String magicdigit;
	String xmlpath;
	String xml;

	public ArrayList<ArrayList<Kouza>> allkouzalist;
	ArrayList<String> nhkkouzaurl;

	/**
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	NHKRadio() throws IOException, ParserConfigurationException, SAXException {

		this.nhkkouzaurl = new ArrayList<String>();
		this.allkouzalist = new ArrayList<ArrayList<Kouza>>();

		// �_�E�����[�h�h�~�p�̏T�ς��f�B���N�g�������擾����
		try {
			this.magicdigit = getDirectoryName();
		} catch (IOException e) {
			Log.d("NHK","�T�ς��̃f�B���N�g�������擾�ł��܂���ł����B");
		}
		
		// ���W�I�p��b
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/kaiwa/"
				+ this.magicdigit + "/listdataflv.xml");
		// ��b�p��P
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/basic1/"
				+ this.magicdigit + "/listdataflv.xml");
		// ��b�p��Q
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/basic2/"
				+ this.magicdigit + "/listdataflv.xml");
		// ��b�p��R
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/basic3/"
				+ this.magicdigit + "/listdataflv.xml");
		// �p��T���ԃg���[�j���O
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/training/"
				+ this.magicdigit + "/listdataflv.xml");
		// ����r�W�l�X�p��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/business1/"
				+ this.magicdigit + "/listdataflv.xml");
		// ���H�r�W�l�X�p��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/english/business2/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ�������
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/chinese/kouza/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ��t�����X��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/french/kouza/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ��C�^���A��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/italian/kouza/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ��n���O���u��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/hangeul/kouza/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ��h�C�c��
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/german/kouza/"
				+ this.magicdigit + "/listdataflv.xml");
		// �܂��ɂ��X�y�C����
		nhkkouzaurl.add("http://www.nhk.or.jp/gogaku/spanish/kouza/"
				+ this.magicdigit + "/listdataflv.xml");

		for (int i = 0; i < nhkkouzaurl.size(); i++) {

			// �u����listdataflv.xml�̃p�X���擾
			xmlpath = nhkkouzaurl.get(i);

			ArrayList<Kouza> kouza = new ArrayList<Kouza>();
			
			// ����u�����P�T�ԕ��擾
			kouza = this.getKouzaList(xmlpath);
			Log.d("NHK", kouza.get(0).getTitle() + "�̂P�T�ԕ��̃f�[�^���擾");

			allkouzalist.add(kouza);

		}

	}


	/**
	 * @param �擾�Ώۂ̌�w�u����URL
	 * @return �P�T�ԕ���Kouza�I�u�W�F�N�g���i�[����ArrayList��Ԃ�
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ArrayList<Kouza> getKouzaList(String xmlurl)
			throws ParserConfigurationException, SAXException, IOException {

		// �h�L�������g�r���_
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlurl);

		// �u���̂P���������Node���擾
		NodeList nl = doc.getElementsByTagName("music");

		ArrayList<Kouza> kouzalist = null;
		kouzalist = new ArrayList<Kouza>();

		for (int i = 0; i < nl.getLength(); i++) {

			// �u���̒�`���擾
			Kouza kouza = new Kouza();

			NamedNodeMap nm = nl.item(i).getAttributes();
			kouza.setTitle(nm.item(0).getNodeValue());
			kouza.setHdate(nm.item(1).getNodeValue());
			kouza.setFilename(nm.item(3).getNodeValue());

			// �u�����X�g�ɒǉ�
			kouzalist.add(kouza);

		}

		return kouzalist;

	}

	/**
	 * @return NHK��w�u���̕�����f�[�^"listdataflv.xml"��String�ŕԂ�
	 * @throws IOException
	 */
	public String getListDataFlvXml() throws IOException {

		Log.d("NHK", "getListDataFlvXml");
		
		// URL���쐬����GET�ʐM���s��
		URL url = new URL(this.xmlpath);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		http.connect();

		// �T�[�o�[����̃��X�|���X��W���o�͂֏o��
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				http.getInputStream()));
		String xml = "", line = "";
		while ((line = reader.readLine()) != null)
			xml += line;
		System.out.println(xml);
		Log.d("NHK", xml);
		reader.close();
		return xml;

	}

	/**
	 * @return �_�E�����[�h�h�~�p�}�W�b�N���[�h��Ԃ�
	 * @throws IOException
	 */
	public String getDirectoryName() throws IOException {

		// URL���쐬����GET�ʐM���s��
		URL url = new URL(
				"http://nhk-rtmp-capture.googlecode.com/svn/trunk/MAGIC-DIGITS.TXT");
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		http.connect();

		// �T�[�o�[����̃��X�|���X��W���o�͂֏o��
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				http.getInputStream()));
		String xml = "", line = "";
		while ((line = reader.readLine()) != null)
			xml += line;
		System.out.println(xml);
		Log.d("NHK", xml);
		reader.close();
		return xml;

	}
}
