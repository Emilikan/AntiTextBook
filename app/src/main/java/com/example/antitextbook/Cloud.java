package com.example.antitextbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Cloud extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);
        Button sendOnCloud = (Button) rootView.findViewById(R.id.buttonSendToCloud);//Кнопка отправить
        sendOnCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Author = ((EditText) getActivity().findViewById(R.id.textAuthorCloud)).getText().toString();
                String Class = ((EditText) getActivity().findViewById(R.id.textClassCloud)).getText().toString();
                String Year = ((EditText) getActivity().findViewById(R.id.textYearCloud)).getText().toString();
                String Subject = ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).getText().toString();
                String Part = ((EditText) getActivity().findViewById(R.id.textPartCloud)).getText().toString();
                if ("".equals(Author)|| "".equals(Class)|| "".equals(Year) || "".equals(Subject) || "".equals(Part)) {
                    Toast.makeText(getActivity(), "Одно из полей не заполненно. Пожалуйста, заполните все поля и повторите отправку", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "Нет ошибки", Toast.LENGTH_LONG).show();
                    //Написать код отправки на сервер
                }
            }
        });
        Button choicePdf = (Button) rootView.findViewById(R.id.buttonDownloadPDF);//Кнопка загрузки pdf
        choicePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdfPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pdfPickerIntent.setType("application/pdf");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        Button choiceImages = (Button) rootView.findViewById(R.id.buttonDownloadImage);//Кнопка загрузки Изображения
        choiceImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
                Uri chosenImageUri = photoPickerIntent.getData();
                Uri file =chosenImageUri ;
                Toast.makeText(getActivity(), file + "", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;

    }



}
