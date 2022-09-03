package ru.agpu.artikproject.background_work.api.SemanticGroup;

public class SemanticGroupItem {
    public String Id;
    public String Name;
    public final String groupId;
    public final String groupIsArchive;
    public final String groupNumberOfStudents;
    public final String groupName;
    public final String groupIsRaspis;

    public SemanticGroupItem(String Id, String Name, String groupId, String groupIsArchive,
                             String groupNumberOfStudents, String groupName, String groupIsRaspis){
        this.Id = Id;
        this.Name = Name;
        this.groupId = groupId;
        this.groupIsArchive = groupIsArchive;
        this.groupNumberOfStudents = groupNumberOfStudents;
        this.groupName = groupName;
        this.groupIsRaspis = groupIsRaspis;
    }
}
