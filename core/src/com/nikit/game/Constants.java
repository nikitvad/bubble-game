package com.nikit.game;

public interface Constants {
    int VIEW_PORT_WIDT = 100;
    int VIEW_PORT_HEIGHT = 100;

    float SPRITE_SCALE = 0.05f;
    float FONT_SCALE_COEF = 0.005f;

    String OBJECT_TYPE_BALL = "object_type_ball";
    String OBJECT_TYPE_EDGE = "object_type_edge";


    String CONTACT_TYPE_UNDEFINED = "contact_type_undefined";
    String CONTACT_TYPE_BALL_EDGE = "contact_type_ball_edge";
    String CONTACT_TYPE_EDGE_BALL = "contact_type_edge_ball";
}
