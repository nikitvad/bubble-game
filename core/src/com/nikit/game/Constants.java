package com.nikit.game;

public interface Constants {
    int VIEW_PORT_WIDT = 50;
    int VIEW_PORT_HEIGHT = 50;

    float SPRITE_SCALE = 0.03f;
    float FONT_SCALE_COEF = 0.005f;

    String OBJECT_TYPE_BALL = "object_type_ball";
    String OBJECT_TYPE_EDGE = "object_type_edge";


    String CONTACT_TYPE_UNDEFINED = "contact_type_undefined";
    String CONTACT_TYPE_BALL_EDGE = "contact_type_ball_edge";
    String CONTACT_TYPE_EDGE_BALL = "contact_type_edge_ball";
    String OBJECT_TYPE_ENEMY = "object_type_enemy";
    Object OBJECT_TYPE_STICK = "object_type_stick";

    String[] BROKEN_STICK_SPRITE_NAMES = {"1", "2", "3", "4"};
    float[] BROKEN_STICK_SPRITE_OFFSETS = {-66, -20, -20, 0};


    float BALL_MASS = 1;
    float ENEMY_MASS = 1;

    float STICK_MASS = 7;
    float[] BROKEN_STICK_MASS_SCALE = {0.15f, 0.45f, 0.25f, 0.15f};

    float FORCE_SCALE = 1500;
    String OBJECT_TYPE_STICK_FRAGMENT="object_type_stick_fragment";
}
