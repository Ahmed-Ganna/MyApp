package com.ganna.faceparse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganna.faceparse.Constants.ParseConstants;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class JokeListAdapter extends BaseAdapter  {
	private LayoutInflater inflater;
	private List<ParseObject> jokes;
    private Context context;

	public JokeListAdapter(Activity activity,Context context, List<ParseObject> jokes) {
		this.jokes = jokes;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
	}

	@Override
	public int getCount() {
		return jokes.size();
	}

	@Override
	public Object getItem(int location) {
		return jokes.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

		if (convertView == null) {
            convertView = inflater.inflate(R.layout.joke_item, null);
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.txt_joke);
            holder.jokeImg = (ImageView) convertView.findViewById(R.id.img_joke);
            convertView.setTag(holder);
        }else {
            ParseObject joke = jokes.get(position);

            holder=(ViewHolder)convertView.getTag();
            holder.description.setText(joke.getString(ParseConstants.DESCRIPTION_COLUMN));

            ParseFile parseFile = joke.getParseFile(ParseConstants.IMAGE_COLUMN);
            String image_url = parseFile.getUrl();

            Picasso
            .with(context)
            .load(image_url)
            .into(holder.jokeImg);

        }
            return convertView;
	}

    public class ViewHolder{

        public TextView description;
        public ImageView jokeImg;
    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

}
