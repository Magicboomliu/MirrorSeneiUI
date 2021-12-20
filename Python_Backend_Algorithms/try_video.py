# Libraries
import cv2
from pose import PoseDetector
import time
from utils.utils import draw_key_points


if __name__ =="__main__":
    
    pose_model = PoseDetector(mode=False,min_detection_confidence=0.5,min_track_confidence=0.5)
    
    cap = cv2.VideoCapture('demo_res/sjw_demo.mp4')
    
    pTime = 0
    while True:
        ret, frame = cap.read()
        if ret == True:
            # Get the results
            results,h,w = pose_model.get_the_landmark(frame)

            # Get pixel(2d), scene(3d) coordinate
            pixel_coord, scene_coord = pose_model.get_scene_and_pixel_coordinate(results,w,h)
           
            # draw the result
            # rendered_image = pose_model.draw_the_landmarks(frame,results)
            rendered_image = draw_key_points(frame,pixel_coord,draw_line=True)

            cTime = time.time()
            fps = 1 / (cTime - pTime)
            pTime = cTime
            cv2.putText(rendered_image, "FPS: "+ str(int(fps)), (70, 100), cv2.FONT_HERSHEY_PLAIN, 3,
                    (255, 0, 0), 3)

            cv2.imshow("cap", rendered_image)
            if cv2.waitKey(5) & 0xff == ord('q'):
                break
        
    cap.release()
    cv2.destroyAllWindows()