package com.k2c.UIComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.legislativecheck.R;

public class HearingItem extends View {

	public HearingItem(Context context) {
		super(context);
	}

	private String title, type, chair, summary;
	private Date date, start_at, end_at;
	private Calendar calendar_s, calendar_e;

	public View createHearingItem(JSONObject object, LayoutInflater inflator) throws JSONException {
		View hearingItem = inflator.inflate(R.layout.item_view, null);
		String stringValue = null;
		TextView textView = null;

		calendar_s = Calendar.getInstance();
		calendar_e = Calendar.getInstance();

		title = object.getString("name");

		if (title != null && title.length() > 0) {
			textView = (TextView) hearingItem.findViewById(R.id.title);
			textView.setText("主題: " + title);
		}

		type = object.getString("type");
		if (type != null && title.length() > 0) {
			textView = (TextView) hearingItem.findViewById(R.id.type);
			textView.setText("類型: " + type);
		}

		chair = object.getString("chair");
		if (chair != null && chair.length() > 0) {
			textView = (TextView) hearingItem.findViewById(R.id.chair);
			textView.setText("主席: " + chair);
		}

		stringValue = object.getString("date");
		if (stringValue != null && stringValue.length() > 0) {
			try {
				date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(stringValue);
				calendar_s.setTime(date);
				calendar_e.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		stringValue = null;

		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy 年 MM 月 dd 日", Locale.getDefault());

			textView = (TextView) hearingItem.findViewById(R.id.date);
			textView.setText("日期: " + df.format(calendar_s.getTime()));
		}

		stringValue = object.getString("time_start");
		if (stringValue != null && stringValue.length() > 0) {

			try {
				start_at = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(stringValue);
				Calendar c = Calendar.getInstance();
				c.setTime(start_at);
				calendar_s.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
				calendar_s.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
				calendar_s.set(Calendar.SECOND, c.get(Calendar.SECOND));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
			textView = (TextView) hearingItem.findViewById(R.id.start_time);
			textView.setText("時間: 從 " + df.format(calendar_s.getTime()));

		}
		stringValue = null;


		stringValue = object.getString("time_end");
		if (stringValue != null && stringValue.length() > 0) {

			try {
				end_at = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(stringValue);
				Calendar c = Calendar.getInstance();
				c.setTime(end_at);
				calendar_e.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
				calendar_e.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
				calendar_e.set(Calendar.SECOND, c.get(Calendar.SECOND));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());

			textView = (TextView) hearingItem.findViewById(R.id.end_time);
			textView.setText("至 "  + df.format(calendar_e.getTime()));
		}
		stringValue = null;

		summary = object.getString("summary");
		if (summary != null && summary.length() > 0) {
			textView = (TextView) hearingItem.findViewById(R.id.summary);
			textView.setText("內容大綱: " + summary);
		}

		Button save = (Button) hearingItem.findViewById(R.id.add_calendar_button);
		if (Calendar.getInstance().getTimeInMillis() < calendar_s.getTimeInMillis()) {
			save.setOnClickListener(clickon);
			save.setText("加到行事曆上");
		} else {
			((ViewGroup)save.getParent()).removeView(save);
		}
		return hearingItem;
	}

	OnClickListener clickon = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, title);
			intent.putExtra(Events.DESCRIPTION, summary);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar_s.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar_e.getTimeInMillis());

			getContext().startActivity(intent);
		}
	};

}
