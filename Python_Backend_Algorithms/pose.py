
# Libraries
import cv2
import numpy
import mediapipe as mp
from numpy.lib.type_check import imag

mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles
mp_pose = mp.solutions.pose



class PoseDetector():
    def __init__(self,mode=False,min_detection_confidence=0.5,min_track_confidence=0.5) -> None:
        
        # Config Settings
        self.mode = mode  # 0 for static image, 1 for video stream
        self.min_detection_confidence = min_detection_confidence
        self.min_track_confidnce = min_track_confidence

        # Load the pose estimation model
        self.model = mp_pose.Pose(
                                static_image_mode = self.mode,
                                min_detection_confidence=self.min_detection_confidence,
                                min_tracking_confidence=self.min_track_confidnce)
        
    def get_the_landmark(self, image):
        image.flags.writeable = False
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        results = self.model.process(image)
        w,h = image.shape[:2]

        return results,w,h
    
    def get_scene_and_pixel_coordinate(self,results,w,h):
        '''
        id : range from 0-32. The index of the human skeleton
        u: the x-axis coordinate in 2d image
        v: the y-axis coordinate in 2d image
        visibility: Whether the keypoint is occuled or not 

        x: the x-axis coordinate in 3d scene ( world)
        y: the y-axis coordinate in 3d scene ( world)
        z : the depth coordinate in 3d scene ( world)



        pixel_coordinate is 4d: [id, u, v,visibility]
        scene_coordinate is 5d :[id,x,y,z,visibility]


        '''

        scene_coordinate = []
        pixel_coordinate = []
        if results.pose_landmarks is not None:
            for idx, predictions in enumerate(results.pose_landmarks.landmark):
                u, v = int(predictions.x * w), int(predictions.y * h)
                pixel_coordinate.append([idx,u,v,predictions.visibility])
                threed_points=[idx,predictions.x,predictions.y,predictions.z,float(predictions.visibility)]
                scene_coordinate.append(threed_points)
        
        return pixel_coordinate, scene_coordinate
        
        

    def draw_the_landmarks(self,image,results):
        image.flags.writeable = True
        # image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        
        mp_drawing.draw_landmarks(
            image,
            results.pose_landmarks,
            mp_pose.POSE_CONNECTIONS,
        landmark_drawing_spec=mp_drawing_styles.get_default_pose_landmarks_style())

        return image
        




    # render_image = pose_model.draw_the_landmarks(image_data,results)
    # cv2.imshow("2d shwing", render_image)
    # cv2.waitKey()
    # cv2.destroyAllWindows()










    

