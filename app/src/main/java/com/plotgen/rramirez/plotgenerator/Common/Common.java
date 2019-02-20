package com.plotgen.rramirez.plotgenerator.Common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.plotgen.rramirez.plotgenerator.Model.Challenge;
import com.plotgen.rramirez.plotgenerator.Model.Character;
import com.plotgen.rramirez.plotgenerator.Model.Genre;
import com.plotgen.rramirez.plotgenerator.Model.Project;
import com.plotgen.rramirez.plotgenerator.Model.Story;
import com.plotgen.rramirez.plotgenerator.Model.User;
import com.plotgen.rramirez.plotgenerator.Model.UserStory;

public class Common {
    public static User currentUser;
    public static Story currentStory;
    public static Challenge currentChallenge;
    public static UserStory currentUserStory;
    public static Character currentCharacter;
    public static Project currentProject;

    public static Story tempStory;
    public static boolean isPAU;
    public static String currentGenre;
    public static UserStory tempUserStory;
    public static boolean charCreationMode;
    public static boolean projectCreationMode;

    public static int onBoarding;
    public static boolean tutorialMODE;


    //Database references
    public static Query currentQuery;
    public static FirebaseDatabase currentDatabase;
    public static DatabaseReference currentReference;
    public static DatabaseReference currentCommentReference;
    public static DatabaseReference currentUserReference;
    public static FirebaseAuth currentAuth;
    public static FirebaseUser currentFirebaseUser;




}
