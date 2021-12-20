import cv2
import sys
sys.path.append("../")
from pose import PoseDetector


def get_2d(pixel_coord_instance):
    return pixel_coord_instance[1:-1]


def get_3d(scene_coord_instance):
    return scene_coord_instance[1:-1]

def draw_line_2d(image,coord,idx1,idx2,color=(0,255,0),thickness=2):
    if coord[idx1][-1] >0.4 and coord[idx2][-1]>0.4:
        cv2.line(image,tuple(get_2d(coord[idx1])),tuple(get_2d(coord[idx2])),color, thickness=thickness)
    


def draw_key_points(image,pixel_2d,draw_line=False,skip_face=True):
    
    if len(pixel_2d)==0:
        return image
    
    for idx,pixels in enumerate(pixel_2d):
        # Skip Face
        if skip_face:
            if idx in [0,1,2,3,4,5,6,7,8,9,10]:
                continue
        u,v = get_2d(pixels)
        # Draw circles
        cv2.circle(image, (u, v), 5, (255, 0, 0), 2)
        # Draw Lines
    
    # draw lines
    if draw_line:
        # Left hand
        draw_line_2d(image,pixel_2d,20,18)
        draw_line_2d(image,pixel_2d,18,16)
        draw_line_2d(image,pixel_2d,16,22)
        draw_line_2d(image,pixel_2d,20,16)

        # Left Small Arm
        draw_line_2d(image,pixel_2d,16,14)

        # Left Big ArM
        draw_line_2d(image,pixel_2d,14,12)

        #Right Hand
        draw_line_2d(image,pixel_2d,21,15)
        draw_line_2d(image,pixel_2d,15,19)
        draw_line_2d(image,pixel_2d,19,17)
        draw_line_2d(image,pixel_2d,15,17)
        
        # Right Small Arm
        draw_line_2d(image,pixel_2d,15,13)

        # Right Big ArM
        draw_line_2d(image,pixel_2d,11,13)

        # Chest
        draw_line_2d(image,pixel_2d,12,11)
        draw_line_2d(image,pixel_2d,11,23)
        draw_line_2d(image,pixel_2d,23,24)
        draw_line_2d(image,pixel_2d,12,24)

        # Left Big Leg
        draw_line_2d(image,pixel_2d,24,26)
        
        # Left small Leg
        draw_line_2d(image,pixel_2d,26,28)

        # Right Big Leg
        draw_line_2d(image,pixel_2d,23,25)
        
        # Right Small Leg
        draw_line_2d(image,pixel_2d,25,27)


        # Left Foot
        draw_line_2d(image,pixel_2d,28,32)
        draw_line_2d(image,pixel_2d,32,30)
        draw_line_2d(image,pixel_2d,28,30)

        #Right Foot
        draw_line_2d(image,pixel_2d,27,29)
        draw_line_2d(image,pixel_2d,27,31)
        draw_line_2d(image,pixel_2d,29,31)

    return image
    

def angle_computation(p1,p2,p3):
    
    pass