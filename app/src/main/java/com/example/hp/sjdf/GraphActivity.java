package com.example.hp.sjdf;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.w3c.dom.Text;

import static android.R.attr.data;

public class GraphActivity extends AppCompatActivity {
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final GraphView graph = (GraphView) findViewById(R.id.graph);
        tv1=(TextView)findViewById(R.id.totalgraph);
        final Intent intent=getIntent();
        final String y=intent.getStringExtra("yes");
        final String n=intent.getStringExtra("no");

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"  Yes","No","      "});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]
        {
                new DataPoint(1, Double.parseDouble(y)),
                new DataPoint(2, Double.parseDouble(n)),
                new DataPoint(3, 0)
        });
        graph.addSeries(series);

        graph.getViewport().setMinY(0);
        // for color
        series.setValueDependentColor(new ValueDependentColor<DataPoint>()
        {
            @Override
            public int get(DataPoint data)
            {
                return Color.rgb((int) data.getX()* 255/2,(int) Math.abs(data.getY()*150),100);
            }
        });

        //series.setSpacing(2);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

    }
}
