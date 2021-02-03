package com.osmacademy.mkpss.Interface;

import com.osmacademy.mkpss.Model.CurrentQuestion;

public interface IQuestion {
    CurrentQuestion getSelectedAnswer(); //get selected answer from user select
    void showCorrectAnswer(); //bold correct answer text
    void disableAnswer();  //disable all check box
    void resetQuestion();  //reset all function on question

}
