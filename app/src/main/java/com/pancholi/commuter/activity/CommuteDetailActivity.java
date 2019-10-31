package com.pancholi.commuter.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.pancholi.commuter.R;
import com.pancholi.commuter.chart.XAxisFormatter;
import com.pancholi.commuter.database.DetailAverage;
import com.pancholi.commuter.viewmodel.DetailViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;

public class CommuteDetailActivity extends BaseActivity {

  static final String EXTRA_COMMUTE_ID = "com.pancholi.commmuter.EXTRA_COMMUTE_ID";

  private DetailViewModel detailViewModel;
  private int commuteId;

  private List<DetailAverage> detailAverages;

  private int minDuration;
  private int maxDuration;

  @BindView(R.id.barChart)
  BarChart barChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_commute_info);
    setViewModel();
  }

  @OnClick(R.id.buttonBack)
  void backButtonPressed() {
    super.onBackPressed();
  }

  private void setViewModel() {
    commuteId = getIntent().getIntExtra(EXTRA_COMMUTE_ID, -1);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    detailViewModel.getAllDetailAveragesForCommute(commuteId)
            .observe(this, this::loadChartData);
  }

  private void loadChartData(List<DetailAverage> detailAverages) {
    for (DetailAverage detailAverage : detailAverages) {
      String time = detailAverage.getTime();
      float distance = detailAverage.getDistanceInMiles();
      float duration = detailAverage.getDurationInHours();

      Log.d(TAG, String.format("Time: %s, Distance: %.2f miles, Duration: %.2f hours",
              time, distance, duration));
    }

    this.detailAverages = detailAverages;

    getMinAndMaxDuration();
//    setXAxisLabels();
//    setYAxisLabels();
    addDataToChart();
  }

  private void getMinAndMaxDuration() {
    float firstDuration = detailAverages.get(0).getDurationInHours();
    float min = firstDuration;
    float max = firstDuration;

    for (int i = 1; i < detailAverages.size(); i++) {
      float duration = detailAverages.get(i).getDurationInHours();

      if (duration < min) {
        min = duration;
      } else if (duration > max) {
        max = duration;
      }
    }

    minDuration = (int) Math.floor(min);
    maxDuration = (int) Math.ceil(max);
  }

  private void setXAxisLabels() {
    XAxis xAxis = barChart.getXAxis();
    xAxis.setLabelCount(13, true);
    xAxis.setGranularity(2f);
    xAxis.setValueFormatter(new XAxisFormatter());
  }

  @SuppressLint("DefaultLocale")
  private void setYAxisLabels() {
    int count = maxDuration - minDuration + 1;
    String[] labels = new String[count];

    int value = minDuration;

    for (int i = 0; i < count; i++) {
      String hours = value == 1 ? "hr" : "hrs";
      labels[i] = String.format("%d %s", value++, hours);
    }

    ValueFormatter formatter = new ValueFormatter() {

      @Override
      public String getAxisLabel(float value, AxisBase axis) {
        return labels[(int) value];
      }
    };

    YAxis yAxis = barChart.getAxisLeft();
    yAxis.setLabelCount(count, true);
    yAxis.setGranularity(1f);
    yAxis.setValueFormatter(formatter);
  }

  private void addDataToChart() {
    BarDataSet set = new BarDataSet(getEntries(), "DurationDataSet");
    BarData data = new BarData(set);

    set.setColor(R.color.colorPrimaryDark);
//    data.setBarWidth(0.2f);
//    barChart.setFitBars(true);
    barChart.getLegend().setEnabled(false);
    barChart.getDescription().setEnabled(false);
    barChart.setDrawGridBackground(false);
    barChart.setDrawValueAboveBar(false);
    barChart.setData(data);
    barChart.animateY(500);
  }

  private List<BarEntry> getEntries() {
    List<BarEntry> entries = new ArrayList<>();
    List<DetailAverage> defaultList = DetailAverage.getDefaultList();

    for (DetailAverage average : defaultList) {
      if (!detailAverages.contains(average)) {
        detailAverages.add(average);
      }
    }

    Collections.sort(detailAverages);

    for (DetailAverage average : detailAverages) {
      entries.add(getBarEntry(average));
    }

    return entries;
  }

  private BarEntry getBarEntry(DetailAverage average) {
    String time = average.getTime();
    float duration = average.getDurationInHours();

    switch (time) {
      case "0700":
        return new BarEntry(0f, duration);
      case "0730":
        return new BarEntry(1f, duration);
      case "0800":
        return new BarEntry(2f, duration);
      case "0830":
        return new BarEntry(3f, duration);
      case "0900":
        return new BarEntry(4f, duration);
      case "0930":
        return new BarEntry(5f, duration);
      case "1000":
        return new BarEntry(6f, duration);
      case "1030":
        return new BarEntry(7f, duration);
      case "1100":
        return new BarEntry(8f, duration);
      case "1130":
        return new BarEntry(9f, duration);
      case "1200":
        return new BarEntry(10f, duration);
      case "1230":
        return new BarEntry(11f, duration);
      case "1300":
        return new BarEntry(12f, duration);
      case "1330":
        return new BarEntry(13f, duration);
      case "1400":
        return new BarEntry(14f, duration);
      case "1430":
        return new BarEntry(15f, duration);
      case "1500":
        return new BarEntry(16f, duration);
      case "1530":
        return new BarEntry(17f, duration);
      case "1600":
        return new BarEntry(18f, duration);
      case "1630":
        return new BarEntry(19f, duration);
      case "1700":
        return new BarEntry(20f, duration);
      case "1730":
        return new BarEntry(21f, duration);
      case "1800":
        return new BarEntry(22f, duration);
      case "1830":
        return new BarEntry(23f, duration);
      case "1900":
        return new BarEntry(24f, duration);
      default:
        return null;
    }
  }
}
