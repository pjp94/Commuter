package com.pancholi.commuter.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class XAxisFormatter extends ValueFormatter {

  private final String[] labels = new String[]
          {
                  "7:00 am", "8:00 am", "9:00 am", "10:00 am", "11:00 am", "12:00 pm",
                  "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", "6:00 pm", "7:00 pm"
          };

  @Override
  public String getAxisLabel(float value, AxisBase axis) {
    return labels[(int) value];
  }
}
