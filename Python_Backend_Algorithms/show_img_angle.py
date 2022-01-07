# Libraries
import cv2
from pose import PoseDetector
from utils.utils import draw_key_points, draw_angles

if __name__ == "__main__":
    pose_model = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)

    image_data = cv2.imread("demo_res/yoga/warrior_ii_5.jpg")

    predefined_angles = ['elbow_left', 'elbow_right',
                         'armpit_left', 'armpit_right',
                         # 'chest_upper_left', 'chest_upper_right',
                         # 'chest_bottom_left', 'chest_bottom_right',
                         # 'left_big_leg_angle', 'right_big_leg_angle',
                         # 'left_foot_angle', 'right_foot_angle',
                         'hip_left', 'hip_right',
                         'left_knee_angle', 'right_knee_angle']

    # h, c, w = image_data.shape

    results, h, w = pose_model.get_the_landmark(image_data)

    # Get pixel(2d), scene(3d) coordinate
    pixel_coord, scene_coord = pose_model.get_scene_and_pixel_coordinate(results, w, h)

    # draw points and lines
    rendered_image = draw_key_points(image_data, pixel_coord, draw_line=True)
    # add angle data
    for pref_angle in predefined_angles:
        rendered_image = draw_angles(rendered_image, scene_coord, pixel_coord, h, w, pref_angle)

    cv2.imshow("example", rendered_image)
    cv2.waitKey(0)
