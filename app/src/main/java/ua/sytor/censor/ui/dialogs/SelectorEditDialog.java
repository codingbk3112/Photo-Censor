package ua.sytor.censor.ui.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thebluealliance.spectrum.SpectrumPalette;

import ua.sytor.censor.R;
import ua.sytor.censor.effects.Effect;
import ua.sytor.censor.selectors.PolygonTouchListener;
import ua.sytor.censor.selectors.RectangleTouchListener;
import ua.sytor.censor.ui.ShapeView;

public class SelectorEditDialog {

    private int selectedColor;

    public SelectorEditDialog(Context context, ShapeView shapeView){

        //Data

        SharedPreferences sharedPreferences = context
                .getSharedPreferences("settings",Context.MODE_PRIVATE);

        //Views

        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_selector_edit, null);

        Spinner spinner = view.findViewById(R.id.spinner);
        String[] selectorTitles = context.getResources().getStringArray(R.array.selector_types);
        spinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,
                selectorTitles));
        spinner.setSelection(sharedPreferences.getInt("selectionType",0));

        SpectrumPalette spectrumPalette  = view.findViewById(R.id.palette);
        spectrumPalette.setColors(new int[]{
            Color.WHITE, Color.BLACK, Color.BLUE, Color.YELLOW, Color.RED, Color.MAGENTA});
        spectrumPalette.setOnColorSelectedListener(color -> selectedColor = color);
        spectrumPalette.setSelectedColor(sharedPreferences.getInt("selectorColor",Color.WHITE));

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(R.string.apply, (dialogInterface, i) -> {
                    switch (spinner.getSelectedItemPosition()){
                        case 0:
                            shapeView.setTouch(new PolygonTouchListener(shapeView));
                            break;
                        case 1:
                            shapeView.setTouch(new RectangleTouchListener(shapeView));
                    }
                    shapeView.setColor(selectedColor);
                    shapeView.resetShape();
                    sharedPreferences.edit()
                            .putInt("selectionType", spinner.getSelectedItemPosition())
                            .putInt("selectorColor",selectedColor)
                            .apply();

                }).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();

    }

}
