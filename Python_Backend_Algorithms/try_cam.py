from pose import PoseDetector
import cv2

if __name__ == "__main__":

    pose_model = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)

    # Camera capture
    cap = cv2.VideoCapture(1)
    # camera open
    while cap.isOpened():
        ret, frame = cap.read()
        if ret == True:
            results, h, w = pose_model.get_the_landmark(frame)
            # draw the result
            rendered_image = pose_model.draw_the_landmarks(frame, results)

            cv2.imshow("cap", rendered_image)
            if cv2.waitKey(5) & 0xff == ord('q'):
                break
    cap.release()
    cv2.destroyAllWindows()
