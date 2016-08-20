clear all;
clc;
load('USPStrainingdata.mat');%����ѵ���������ݿ�
[charNumbers,dimension]=size(traindata);%����������ݿ��������и���charNumbers��ÿ���ַ�����ά��dimension
image=zeros(16,16);%��ʼ����һ��ͼ��
P=mat2gray(traindata);%ת��Ϊ�Ҷ�ͼ��
fid = fopen('usps.bin','wb');
startx=0;
overx=0;
len=0;
for k=1:50
    class=find(traintarg(k,:)==1)-1;%��ȡ��ǰ�ַ������
    for i=1:16
        for j=1:16
            image(i,j)=P(k,(i-1)*16+j);
        end
    end
    th= graythresh(image);%��Ҷ�ͼ�����ֵth
    I=im2bw(image,th);%��ֵ���ַ�ͼ��
    I=~I;
    %      g=bwmorph(I,'thin',1);%ϸ����ֵ��ͼ��
    %      subplot(5,10,k),imshow(~I);%��ʾ�ַ�ͼ��
    fwrite(fid,class,'uchar'); %��¼�ַ�ͼ������
    fwrite(fid,255,'uchar'); %��¼�ַ�ͼ������
    fwrite(fid,255,'uchar'); %��¼�ַ�ͼ������
    for i=1:16
        for j=1:16
            pixel=I(i,j);
            if (~startx & ~overx & pixel==0)
                beginx = j-1;
                beginy = i-1;
                startx = 1;
            end
            %������һ����ɫ����
            if (~overx & startx && pixel==1)

                endx = j-1;
                overx = 1;
            end
            %�Ѿ�������β
            if (~overx & startx & pixel==0 && j==16)

                endx = j-1;
                overx = 1;
            end
            if (startx==1 && overx==1)
                length = endx - beginx;
                startx = 0;
                overx = 0;
                fwrite(fid,beginx,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
                fwrite(fid,beginy,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
                fwrite(fid,length,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
                length=0;
                found=1;
            end
        end
    end
    %д�������־
    if (found==1)
        fwrite(fid,0,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
        fwrite(fid,0,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
        fwrite(fid,0,'uchar'); %д��ͼ��Ŀ�Ⱥ͸߶�
    end
end
fclose(fid);







