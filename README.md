### chapter7takeVideo
##第七次作业 模拟拍图片和视频,效果如图：<br>
#1.take a picture<br>
![img](/screenshots/1.jpg)<br>
#2.take a video<br>
![img](/screenshots/3.jpg)<br>
![img](/screenshots/2.jpg)<br>
#3.实时拍照和录屏,并在手机中存储<br>
![img](/screenshots/4.jpg)<br>
主要实现代码:<br>
//todo 拍一张照片<br>
mCamera.takePicture(null,null,mPicture);<br>
 //todo 录制视频<br>
 
 
                isRecording = true;
                mMediaRecorder =new MediaRecorder();
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
                mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
                mMediaRecorder.setOrientationHint(rotationDegree);
                try {
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    Log.d("录制了视频","录制了视频");
                } catch (IOException e) {
                    releaseMediaRecorder();
                    e.printStackTrace();
                }
                
//todo 切换前后摄像头<br>

             if (CAMERA_TYPE== Camera.CameraInfo.CAMERA_FACING_BACK){
                releaseCameraAndPreview();
                CAMERA_TYPE=Camera.CameraInfo.CAMERA_FACING_FRONT;
                mCamera=getCamera(CAMERA_TYPE);
                mCamera.stopPreview();
                startPreview(surfaceHolder);
             }else {
                releaseCameraAndPreview();
                CAMERA_TYPE=Camera.CameraInfo.CAMERA_FACING_BACK;
                mCamera=getCamera(CAMERA_TYPE);
                mCamera.stopPreview();
                startPreview(surfaceHolder);
            }                



