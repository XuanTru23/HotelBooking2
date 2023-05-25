package com.example.hotelbooking.Activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;


public class CustomTextWatcher implements TextWatcher {
    private final EditText editText;
    private final DecimalFormat decimalFormat;

    public CustomTextWatcher(EditText editText) {
        this.editText = editText;
        this.decimalFormat = new DecimalFormat("#,###");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Không cần xử lý trước khi văn bản thay đổi
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Không cần xử lý khi văn bản thay đổi
    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        String originalInput = s.toString();

        // Xóa các ký tự không phải số và dấu "."
        String cleanInput = originalInput.replaceAll("[^\\d]", "");

        // Định dạng số theo định dạng "1.000.000"
        try {
            double amount = Double.parseDouble(cleanInput);
            String formattedInput = decimalFormat.format(amount);
            editText.setText(formattedInput);
            editText.setSelection(formattedInput.length());
        } catch (NumberFormatException e) {
            editText.setText("");
        }

        editText.addTextChangedListener(this);
    }
}
