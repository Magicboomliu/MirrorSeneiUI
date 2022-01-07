# Libraries
import cv2
from pose import PoseDetector
from utils.utils import draw_angles, draw_key_points, neighbour_dir, MotionInitialCheck, ShowStatus
import numpy as np

if __name__ == "__main__":
    pose_model_1 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)
    pose_model_2 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)

    # Image path
    sample_image_1 = "demo_res/yoga/warrior_ii_1.jpg"
    sample_image_2 = "demo_res/yoga/warrior_ii_3.jpg"

    # image data
    image_data_sample_1 = cv2.imread(sample_image_1)
    image_data_sample_2 = cv2.imread(sample_image_2)

    # Flip or not : For testing
    # image_data_sample_2 = cv2.flip(image_data_sample_2, 1)

    # Get the Image size
    # h, c, w = image_data_sample_1.shape

    # For reference Image data result
    reference_results, h, w = pose_model_1.get_the_landmark(image_data_sample_1)
    # Get pixel(2d), scene(3d) coordinate
    pixel_coord_ref, scene_coord_ref = pose_model_1.get_scene_and_pixel_coordinate(reference_results, w, h)
    # draw reference image
    rendered_image_ref = draw_key_points(image_data_sample_1, pixel_coord_ref, draw_line=True)

    # For the query image data result
    query_results, h, w = pose_model_2.get_the_landmark(image_data_sample_2)
    # Get pixel(2d), scene(3d) coordinate
    pixel_coord_q, scene_coord_q = pose_model_2.get_scene_and_pixel_coordinate(query_results, w, h)
    # draw query image
    rendered_image_q = draw_key_points(image_data_sample_2, pixel_coord_q, draw_line=True)

    # initial different check
    diff = MotionInitialCheck(coord_ref=scene_coord_ref, coord_query=scene_coord_q, neighbour_dir=neighbour_dir,
                              idx_list=[11, 12, 24, 23, 14, 13, 26, 25, 16, 15, 28, 27])

    # TODO : Status is 1 : begin Refine?

    rendered_image_q = ShowStatus(diff, rendered_image_q, h, w)

    cv2.imshow("image ref", rendered_image_ref)
    cv2.imshow("image query", rendered_image_q)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
