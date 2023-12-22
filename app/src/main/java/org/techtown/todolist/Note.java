package org.techtown.todolist;

public class Note {
    int _id;
    String todo;
    int days;
    String today;
    String date;

    public Note(int _id, String todo, int days, String today, String date){
        this._id = _id;
        this.todo = todo;
        this.days = days;
        this.today = today;
        this.date = date;
    }

    public int getId() {
        return _id;
    }
    public void setId(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return todo;
    }
    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        this.days = days;
    }

    public String getToday() {
        return today;
    }
    public void setToday(String today) {
        this.today = today;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
