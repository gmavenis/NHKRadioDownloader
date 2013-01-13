package com.watchan.nhkradiodownloader;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.watchan.nhkradiodownloader.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NHKRadioDownloader extends Activity {

	public NHKRadio radio;
	public NotificationManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nhkradio_downloader);

		try {
			radio = new NHKRadio();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		// �A�C�e����ǉ����܂�

		for (int i = 0; i < radio.allkouzalist.size(); i++) {
			adapter.add(radio.allkouzalist.get(i).get(0).getTitle());
		}

		ListView listView = (ListView) findViewById(R.id.listView1);
		// �A�_�v�^�[��ݒ肵�܂�
		listView.setAdapter(adapter);
		// ���X�g�r���[�̃A�C�e�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listView = (ListView) parent;
				// �N���b�N���ꂽ�A�C�e�����擾���܂�
				String item = (String) listView.getItemAtPosition(position);
				Toast.makeText(NHKRadioDownloader.this, item, Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent(NHKRadioDownloader.this, KouzaList.class);
				intent.putExtra("title", item);
				intent.putExtra("radio", radio);
				startActivity(intent);

			}
		});
		// ���X�g�r���[�̃A�C�e�����I�����ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listView = (ListView) parent;
				// �I�����ꂽ�A�C�e�����擾���܂�
				String item = (String) listView.getSelectedItem();
				Toast.makeText(NHKRadioDownloader.this, item, Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_nhkradio_downloader, menu);
		return true;

	}

}
