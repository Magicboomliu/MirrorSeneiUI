from os import stat
from tkinter import *
from PIL import Image,ImageTk
import cv2
from cv2 import VideoCapture
import numpy as np


from pose import PoseDetector
from utils.utils import neighbour_dir
from utils.utils import draw_key_points, neighbour_dir
from utils.utils import MotionInitialCheck, ShowStatus
from utils.utils import show_predefined_angles, draw_angles
from utils.utils import is_angle_wrong, draw_wrong_angle, get_2d, get_3d,motion_similarity

import time
from tkinter.filedialog import askopenfilename


def feedback_face(render_image,type,H,W):
    if type ==1:
        Roi_data = FeedFaceData
    if type ==2:
        Roi_data = FeedFaceData2
    if type ==3:
        Roi_data = FeedFaceData3

    # Mask Image
    lower_green = np.array([0,0,0]) 
    upper_green = np.array([0,0,0])
    mask = cv2.inRange(Roi_data, lower_green, upper_green)

    masked_image = np.copy(Roi_data)
    masked_image[mask != 0] = [0, 0, 0]

    # Take Place image
    zz = np.array(render_image)
    zz_roi= zz[0:80,W-1-80:W-1,:]

    crop_background = np.copy(zz_roi)
    crop_background[mask == 0] = [0,0,0]
    complete_image = masked_image + crop_background
    
    zz[0:80,W-1-80:W-1,:] = complete_image
    return zz
    
def ShowLogo(logo_data,rendered_data):
    # Mask Image
    lower_green = np.array([240,240,240]) 
    upper_green = np.array([255,255,255])
    mask = cv2.inRange(logo_data, lower_green, upper_green)

    masked_image = np.copy(logo_data)
    masked_image[mask != 0] = [0, 0, 0]

    # Take Place image
    zz = np.array(rendered_data)
    zz_roi= zz[0:120,0:140]

    crop_background = np.copy(zz_roi)
    crop_background[mask == 0] = [0,0,0]
    complete_image = masked_image + crop_background
    
    zz[0:120,0:140] = complete_image
    return zz

    
def motion1():
    btn_motion1.configure(bg='DarkSeaGreen')
    btn_motion2.configure(bg='Tan')
    btn_motion3.configure(bg='Tan')
    btn_motion4.configure(bg='Tan')
    btn_motion5.configure(bg='Tan')
    initial(1)

def motion2():
    btn_motion2.configure(bg='DarkSeaGreen')
    btn_motion1.configure(bg='Tan')
    btn_motion3.configure(bg='Tan')
    btn_motion4.configure(bg='Tan')
    btn_motion5.configure(bg='Tan')
    initial(2)

def motion3():
    btn_motion3.configure(bg='DarkSeaGreen')
    btn_motion2.configure(bg='Tan')
    btn_motion1.configure(bg='Tan')
    btn_motion4.configure(bg='Tan')
    btn_motion5.configure(bg='Tan')
    initial(3)

def motion4():
    btn_motion4.configure(bg='DarkSeaGreen')
    btn_motion2.configure(bg='Tan')
    btn_motion3.configure(bg='Tan')
    btn_motion1.configure(bg='Tan')
    btn_motion5.configure(bg='Tan')
    initial(4)

def motion5():
    btn_motion5.configure(bg='DarkSeaGreen')
    btn_motion2.configure(bg='Tan')
    btn_motion3.configure(bg='Tan')
    btn_motion4.configure(bg='Tan')
    btn_motion1.configure(bg='Tan')
    initial(5)


def video_loop():
    success, img = camera.read()  # 从摄像头读取照片
    if success:
        cv2.waitKey(10)

        img = ShowLogo(MSLogoData,img)


        H,W = img.shape[:2]

        reference_data = np.array(ImageTk.getimage(referenceView.imtk))
     
    

        # For reference Image data result
        reference_results, h_r, w_r = pose_model_1.get_the_landmark(reference_data)
        # Get pixel(2d), scene(3d) coordinate
        pixel_coord_ref, scene_coord_ref = pose_model_1.get_scene_and_pixel_coordinate(reference_results, w_r, h_r)
        

        # For the query image data result
        query_results, h_q, w_q = pose_model_2.get_the_landmark(img)
        # Get pixel(2d), scene(3d) coordinate
        pixel_coord_q, scene_coord_q = pose_model_2.get_scene_and_pixel_coordinate(query_results, w_q, h_q)
        
        
        # draw query image
        rendered_image_q = draw_key_points(img, pixel_coord_q, draw_line=True)

        for pref_angle in predefined_angles:
            rendered_image_q = draw_wrong_angle(rendered_image_q, scene_coord_ref, scene_coord_q, pixel_coord_q,
                                            pref_angle, threshold=30)

        if len(pixel_coord_q) ==0 or len(pixel_coord_ref)==0:
            status = False
            angle_dict = dict()
        if len(pixel_coord_ref)!=len(pixel_coord_q):
            status = False
            angle_dict = dict()
        else:
            status, angle_dict = motion_similarity(pixel_coord_ref, pixel_coord_q, neighbour_dir=neighbour_dir,
                                           idx_list=idx_list,predefine_angle_list=predefined_angles, threshold=30)

        state = "Correct Motion!"
        if status==True:
            state = "Correct Motion!"
            d = feedback_face(rendered_image_q,0,H,W)
        else:
            if len(angle_dict) ==0:
                state = "Please Follow"
                d = feedback_face(rendered_image_q,1,H,W)
            else:
                state = "Need Adjustment"
                d = feedback_face(rendered_image_q,2,H,W)
        

        
        cv2image = cv2.resize(d,(600,450))
        cv2.putText(cv2image,'{}'.format(state),(220,50),cv2.FONT_HERSHEY_COMPLEX,1,(255,0,0),2)
        cv2image = cv2.cvtColor(cv2image, cv2.COLOR_BGR2RGBA)#转换颜色从BGR到RGBA
        current_image = Image.fromarray(cv2image)#将图像转换成Image对象
        imgtk = ImageTk.PhotoImage(image=current_image)
        CameraView.imgtk = imgtk
        CameraView.config(image=imgtk)
        # 特定时间后重复执行一个函数
        root.after(1, video_loop)

def initial(type):
    if type ==1:
        image_path ="demo_res/video_test/1.png"
    if type ==2:
        image_path ="demo_res/video_test/2.png"
    if type ==3:
        image_path ="demo_res/video_test/3.png"
    if type ==4:
        image_path ="demo_res/video_test/4.png"
    if type ==5:
        image_path ="demo_res/video_test/5.png"
    
    NewReferenceData = cv2.imread(image_path)
    NewReferenceData = cv2.resize(NewReferenceData,(300,200))
    cv2.putText(NewReferenceData,'Yoga standard motion {}'.format(type),(55,195),cv2.FONT_HERSHEY_COMPLEX,0.5,(255,0,0),1)
    NewReferenceData = cv2.cvtColor(NewReferenceData, cv2.COLOR_BGR2RGBA)#转换颜色从BGR到RGBA
    NewReferenceData = Image.fromarray(NewReferenceData)#将图像转换成Image对象
    nimgtk = ImageTk.PhotoImage(image=NewReferenceData)
    referenceView.imtk = nimgtk
    referenceView.config(image=nimgtk)





if __name__ =="__main__":
    
    

        

    pose_model_1 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)
    pose_model_2 = PoseDetector(mode=False, min_detection_confidence=0.5, min_track_confidence=0.5)
    predefined_angles = ['elbow_left', 'elbow_right',
                     'armpit_left', 'armpit_right',
                     'hip_left', 'hip_right',
                     'left_knee_angle', 'right_knee_angle']

    idx_list = [11, 12, 24, 23, 14, 13, 26, 25, 16, 15, 28, 27]

    
    
    
    root = Tk()
    root.title("MirrorSensei")
    root.configure(bg='white')
    root.geometry("600x700")
    path = StringVar()

    file_entry = Entry(root, state='readonly', text=path)

    imagedata = cv2.imread("../resources/holder.jpg")
    cv2image = cv2.cvtColor(imagedata, cv2.COLOR_BGR2RGBA)#转换颜色从BGR到RGBA

    cv2image = cv2.resize(cv2image,(300,200))
    current_image = Image.fromarray(cv2image)#将图像转换成Image对象
    imgtk = ImageTk.PhotoImage(image=current_image)


    MSLogoData = cv2.imread("demo_res/logo.png")
    MSLogoData = cv2.resize(MSLogoData,(140,120))


    # FaceData1
    FeedFaceData = cv2.imread("demo_res/wait.png")
    FeedFaceData = cv2.resize(FeedFaceData,(80,80))
    
    # FaceData2
    FeedFaceData2 = cv2.imread("demo_res/crying.png")
    FeedFaceData2 = cv2.resize(FeedFaceData2,(80,80))

    # FaceData3
    FeedFaceData3 = cv2.imread("demo_res/laugh.png")
    FeedFaceData3 = cv2.resize(FeedFaceData3,(80,80))




    fm1 = Frame(root)
    fm1.configure(bg='white')
    fm1.pack(side=TOP, fill=BOTH, expand=NO)
    # Reference Image
    logo = Label(fm1)
    logo.imgtk = imgtk
    logo.config(image=imgtk)
    # Size is 400,120
    logo.pack(side=LEFT, fill=Y, expand=NO)
    referenceView = Label(fm1)
    NewReferenceData = cv2.imread("demo_res/video_test/1.png")
    initial(1)
    referenceView.pack(side=LEFT, fill=Y, expand=YES)
    
    # Camera View
    fm2 = Frame(root)
    fm2.configure(bg='white')
    fm2.pack(side=TOP, fill=BOTH, expand=NO)
    CameraView = Label(fm2)
    


  
    CameraView.pack(side=RIGHT, fill=Y, expand=YES)
    
    camera = cv2.VideoCapture(0)    #摄像头

    
    

    fm3 = Frame(root)
    fm3.configure(bg='white')
    fm3.pack(side=TOP, fill=BOTH, expand=NO)

    # Motion1
    btn_motion1 = Button(fm3,text='Motion1',command=motion1,height=2,width=16,fg='white',bg='DarkSeaGreen',activebackground='HotPink',activeforeground='white')
    
    btn_motion1.pack(side=LEFT, fill=Y, expand=YES)

    # Motion2
    btn_motion2 = Button(fm3,text='Motion2',command=motion2,height=2,width=16,fg='white',bg='Tan',activebackground='HotPink',activeforeground='white')
    btn_motion2.pack(side=LEFT, fill=Y, expand=YES)

    # Motion3
    btn_motion3 = Button(fm3,text='Motion3',command=motion3,height=2,width=16,fg='white',bg='Tan',activebackground='HotPink',activeforeground='white')
    btn_motion3.pack(side=LEFT, fill=Y, expand=YES)

    # Motion4
    btn_motion4 = Button(fm3,text='Motion4',command=motion4,height=2,width=16,fg='white',bg='Tan',activebackground='HotPink',activeforeground='white')
    btn_motion4.pack(side=LEFT, fill=Y, expand=YES)
    
    # Motion5
    btn_motion5 = Button(fm3,text='Motion5',command=motion5,height=2,width=16,fg='white',bg='Tan',activebackground='HotPink',activeforeground='white')
    btn_motion5.pack(side=LEFT, fill=Y, expand=YES)


    video_loop()

  



    root.mainloop()

    camera.release()
    cv2.destroyAllWindows()




    
    
    
