package ru.agpu.artikproject.background_work.api.GetRaspisForGroup;

public class ParaOnePodgroupItem {
    public String Id;
    public String Auditoriya;

    public ParaOnePodgroupItem(String id, String auditoriya, String gruppa, String weekType,
                               String podgruppa, String disciplineId, String disciplineName,
                               String prepodFIO, String comment, String paraTypeColor,
                               String dayNumber, String tipZanyatiya, String paraNummber,
                               String isCollisionClassroom, String isCollisionTeacher) {
        Id = id;
        Auditoriya = auditoriya;
        Gruppa = gruppa;
        WeekType = weekType;
        Podgruppa = podgruppa;
        DisciplineId = disciplineId;
        DisciplineName = disciplineName;
        PrepodFIO = prepodFIO;
        Comment = comment;
        ParaTypeColor = paraTypeColor;
        DayNumber = dayNumber;
        TipZanyatiya = tipZanyatiya;
        ParaNummber = paraNummber;
        IsCollisionClassroom = isCollisionClassroom;
        IsCollisionTeacher = isCollisionTeacher;
    }

    public String Gruppa;
    public String WeekType;
    public String Podgruppa;
    public String DisciplineId;
    public String DisciplineName;
    public String PrepodFIO;
    public String Comment;
    public String ParaTypeColor;
    public String DayNumber;
    public String TipZanyatiya;
    public String ParaNummber;
    public String IsCollisionClassroom;
    public String IsCollisionTeacher;
}
