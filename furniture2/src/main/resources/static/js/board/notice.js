let fileList;

(()=>{

})()

function openFileInput() {
    // 파일 입력(input type="file") 요소 클릭
    document.getElementById('fileInput').value = '';
    document.getElementById('fileInput').click();
}

function handleFileSelect(event) {
    const files = document.getElementById('fileInput').files;
    fileList = files;

    const noticeFileBox = document.getElementById('noticeFileBox');
    noticeFileBox.innerHTML = '';

    Array.from(files).forEach((file, index)=>{
        const fileContainer = document.createElement('div');
        fileContainer.classList.add('mt-2', 'flex', 'items-center');

        const fileNameSpan = document.createElement('span');
        fileNameSpan.classList.add('text-sm');
        fileNameSpan.textContent = file.name;

        const deleteButton = document.createElement('button');
        deleteButton.classList.add(
            'inline-flex', 'items-center', 'justify-center',
            'whitespace-nowrap', 'rounded-md', 'text-sm',
            'font-medium', 'ring-offset-background',
            'transition-colors', 'focus-visible:outline-none',
            'focus-visible:ring-2', 'focus-visible:ring-ring',
            'focus-visible:ring-offset-2', 'disabled:pointer-events-none',
            'disabled:opacity-50', 'hover:bg-accent', 'hover:text-accent-foreground',
            'h-10', 'px-4', 'py-2', 'ml-2'
        );
        deleteButton.textContent = 'X';

        deleteButton.onclick = () => {

            // UI에서 삭제
            fileContainer.remove();

            const newFileList = Array.from(files).filter(deleteFile => deleteFile.name !== file.name);
            fileList = newFileList

        };

        fileContainer.appendChild(fileNameSpan);
        fileContainer.appendChild(deleteButton);

        noticeFileBox.appendChild(fileContainer);
    });


}