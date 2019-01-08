package com.example.antitextbook;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

public class BookForRecycle {
    private String author;
    private String subject;
    private String describing;
    private String classOfBook;
    private String imageForBook;
    private ArrayList<Integer> arrayList;
    private Context context;
    private FragmentActivity activity;
    private String nameOfSchool;
    private Boolean isAdmin = false;
    private Boolean isBook = false;

    public BookForRecycle(String author, String subject, String describing, String classOfBook, String image,
                          ArrayList<Integer> arrayList, Context context, FragmentActivity activity, String nameOfSchool, Boolean isBook){
        this.author = author;
        this.arrayList = arrayList;
        this.subject = subject;
        this.describing = describing;
        this.classOfBook = classOfBook;
        this.imageForBook = image;
        this.context = context;
        this.activity = activity;
        this.isBook = isBook;
        this.nameOfSchool = nameOfSchool;
    }
    public BookForRecycle(String author, String subject, String describing, String classOfBook, String image,
                          ArrayList<Integer> arrayList, Context context, FragmentActivity activity, Boolean isBook, Boolean isAdmin){
        this.author = author;
        this.arrayList = arrayList;
        this.subject = subject;
        this.describing = describing;
        this.classOfBook = classOfBook;
        this.imageForBook = image;
        this.context = context;
        this.activity = activity;
        this.isAdmin = isAdmin;
        this.isBook = isBook;
    }

    public BookForRecycle(String author, String subject, String describing, String classOfBook, String image,
                          ArrayList<Integer> arrayList, Context context, FragmentActivity activity, Boolean isBook){
        this.author = author;
        this.arrayList = arrayList;
        this.subject = subject;
        this.describing = describing;
        this.classOfBook = classOfBook;
        this.imageForBook = image;
        this.context = context;
        this.activity = activity;
        this.isBook = isBook;
    }

    public Boolean getIsBook(){
        return isBook;
    }

    public Boolean getIsAdmin(){
        return isAdmin;
    }

    public String getNameOfSchool(){
        return nameOfSchool;
    }

    public FragmentActivity getActivity(){
        return activity;
    }

    public Context getContext(){
        return context;
    }

    public ArrayList<Integer> getArrayList(){
        return arrayList;
    }

    public void setArrayList(ArrayList<Integer> arrayList){
        this.arrayList = arrayList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescribing() {
        return describing;
    }

    public void setDescribing(String describing) {
        this.describing = describing;
    }

    public String getClassOfBook() {
        return classOfBook;
    }

    public void setClassOfBook(String classOfBook) {
        this.classOfBook = classOfBook;
    }

    public String getImageForBook() {
        return imageForBook;
    }

    public void setImageForBook(String imageForBook) {
        this.imageForBook = imageForBook;
    }
}
