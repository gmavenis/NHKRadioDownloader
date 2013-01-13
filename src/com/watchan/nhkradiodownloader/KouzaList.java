package com.watchan.nhkradiodownloader;

import com.schriek.rtmpdump.*;
import java.util.ArrayList;

import com.watchan.nhkradiodownloader.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KouzaList extends Activity implements Runnable {

	NHKRadio radio;
	int kouzanum;
	String command;
	private static ProgressDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kouza_list);

		TextView textview = (TextView) findViewById(R.id.textView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		final NHKRadio radio = (NHKRadio) getIntent().getExtras().get("radio");
		String title = (String) getIntent().getExtras().get("title");
		// �A�C�e����ǉ����܂�

		for (int i = 0; i < radio.allkouzalist.size(); i++) {
			ArrayList<Kouza> kouzalist = radio.allkouzalist.get(i);

			if (kouzalist.get(0).getTitle().equals(title)) {
				// kouzanum = i;
				textview.setText(title);

				for (int j = 0; j < kouzalist.size(); j++) {
					adapter.add(kouzalist.get(j).getHdate());
				}

				break;
			}
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

				ArrayList<Kouza> kouzalist = radio.allkouzalist.get(kouzanum);
				for (int i = 0; i < kouzalist.size(); i++) {

					Kouza kouza = kouzalist.get(i);

					if (kouza.getHdate().equals(item)) {

						command = "rtmpdump -r rtmp://flv9.nhk.or.jp/flv9/_definst_/gogaku/streaming/flv/"
								+ radio.magicdigit
								+ "/"
								+ kouza.getFilename()
								+ " -o /sdcard/NHK/"
								+ "["
								+ kouza.getTitle()
								+ "]" + kouza.getHdate() + ".flv";

						Log.d("NHK", item + "=" + kouzalist.get(i).getHdate());
						Log.d("NHK", command);

						// �v���O���X�_�C�A���O�̐ݒ�
						waitDialog = new ProgressDialog(KouzaList.this);
						// �v���O���X�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
						waitDialog.setMessage("[" + kouza.getTitle() + "]\n"
								+ kouza.getHdate() + "�_�E�����[�h��...");
						// �~�X�^�C���i���邭����^�C�v�j�ɐݒ肵�܂�
						waitDialog
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						// �v���O���X�_�C�A���O��\��
						waitDialog.show();

						Thread thread = new Thread(KouzaList.this);
						/*
						 * show()���\�b�h�Ńv���O���X�_�C�A���O��\�����A �ʃX���b�h���g���A���ŏd���������s���B
						 */
						thread.start();

						break;
					}
				}

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
				Toast.makeText(KouzaList.this, item, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	@Override
	public void run() {
		// �_�C�A���O���������茩����悤�ɏ��������X���[�v
		// �innn�F�C�ӂ̃X���[�v���ԁE�~���b�P�ʁj
		Rtmpdump dump = new Rtmpdump();
		dump.parseString(command);
		// run����UI�̑�������Ă��܂��ƁA��O����������ׁA
		// Handler�Ƀo�g���^�b�`
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// Handler�N���X�ł�Activity���p�����ĂȂ�����
			// �ʂ̐e�N���X�̃��\�b�h�ɂď������s���悤�ɂ����B
			// YYY();

			// �v���O���X�_�C�A���O�I��
			waitDialog.dismiss();
			Toast.makeText(KouzaList.this, "�_�E�����[�h����", Toast.LENGTH_LONG)
					.show();

			waitDialog = null;
		}
	};

}
