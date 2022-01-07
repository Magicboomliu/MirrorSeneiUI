# Libraries
import cv2
from pose import PoseDetector
from utils.utils import neighbour_dir
from utils.utils import draw_key_points, neighbour_dir
from utils.utils import MotionInitialCheck, ShowStatus
from utils.utils import show_predefined_angles, draw_angles
from utils.utils import is_angle_wrong, draw_wrong_angle, get_2d, get_3d,motion_similarity
import numpy as np

predefined_angles = ['elbow_left', 'elbow_right',
                     'armpit_left', 'armpit_right',
                     'hip_left', 'hip_right',
                     'left_knee_angle', 'right_knee_angle']

idx_list = [11, 12, 24, 23, 14, 13, 26, 25, 16, 15, 28, 27]

if __name__ == "__main__":
    pose_model_1 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)
    pose_model_2 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)

    # Image path
    sample_image_1 = "demo_res/yoga/warrior_ii_1.jpg"
    sample_image_2 = "demo_res/yoga/warrior_ii_2.jpg"

    # image data
    image_data_sample_1 = cv2.imread(sample_image_1)
    image_data_sample_2 = cv2.imread(sample_image_2)

    # For reference Image data result
    reference_results, h_r, w_r = pose_model_1.get_the_landmark(image_data_sample_1)
    # Get pixel(2d), scene(3d) coordinate
    pixel_coord_ref, scene_coord_ref = pose_model_1.get_scene_and_pixel_coordinate(reference_results, w_r, h_r)
    # draw reference image
    rendered_image_ref = draw_key_points(image_data_sample_1, pixel_coord_ref, draw_line=True)

    # For the query image data result
    query_results, h_q, w_q = pose_model_2.get_the_landmark(image_data_sample_2)
    # Get pixel(2d), scene(3d) coordinate
    pixel_coord_q, scene_coord_q = pose_model_2.get_scene_and_pixel_coordinate(query_results, w_q, h_q)
    # draw query image
    rendered_image_q = draw_key_points(image_data_sample_2, pixel_coord_q, draw_line=True)

    # # initial different check
    # diff = MotionInitialCheck(coord_ref=scene_coord_ref, coord_query=scene_coord_q, neighbour_dir=neighbour_dir,
    #                           idx_list=[11, 12, 24, 23, 14, 13, 26, 25, 16, 15, 28, 27])
    #
    # rendered_image_q = ShowStatus(diff, rendered_image_q, h_q, w_q)
    # mode = 'left_knee_angle'
    # print(is_angle_wrong(scene_coord_ref, scene_coord_q, mode))

    for pref_angle in predefined_angles:
        rendered_image_q = draw_wrong_angle(rendered_image_q, scene_coord_ref, scene_coord_q, pixel_coord_q,
                                            pref_angle, threshold=30)

    status, angle_dict = motion_similarity(pixel_coord_ref, pixel_coord_q, neighbour_dir=neighbour_dir,
                                           idx_list=idx_list,predefine_angle_list=predefined_angles, threshold=30)

    print(status, angle_dict)
    # show pics
    cv2.imshow("image ref", rendered_image_ref)
    cv2.imshow("image query", rendered_image_q)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
