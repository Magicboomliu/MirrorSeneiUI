# Libraries
import cv2
from pose import PoseDetector
from utils.utils import draw_angles,draw_key_points

if __name__ =="__main__":

    pose_model = PoseDetector(mode=False,min_detection_confidence=0.5,min_track_confidence=0.5)

    sample_image ='demo.jpeg'
    image_data = cv2.imread("demo_res/demo.jpeg")
    
    h,c,w = image_data.shape
    
    results,h,w = pose_model.get_the_landmark(image_data)


    # Get pixel(2d), scene(3d) coordinate
    pixel_coord, scene_coord = pose_model.get_scene_and_pixel_coordinate(results,w,h)


    rendered_image = draw_key_points(image_data,pixel_coord,draw_line=True)

    cv2.imshow("image example",rendered_image)

    cv2.waitKey(0)
    cv2.destroyAllWindows()